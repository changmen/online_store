package com.example.onlinestore.service.impl;

import com.example.onlinestore.bean.Address;
import com.example.onlinestore.bean.Member;
import com.example.onlinestore.bean.PointRecord;
import com.example.onlinestore.bean.PointRule;
import com.example.onlinestore.dto.AddressRequest;
import com.example.onlinestore.dto.LoginRequest;
import com.example.onlinestore.dto.LoginResponse;
import com.example.onlinestore.dto.MemberRegistryRequest;
import com.example.onlinestore.entity.AddressEntity;
import com.example.onlinestore.entity.MemberEntity;
import com.example.onlinestore.entity.PointRecordEntity;
import com.example.onlinestore.entity.PointRuleEntity;
import com.example.onlinestore.enums.PointRecordType;
import com.example.onlinestore.enums.PointRuleStatus;
import com.example.onlinestore.errors.ErrorCode;
import com.example.onlinestore.exceptions.BizException;
import com.example.onlinestore.mapper.AddressMapper;
import com.example.onlinestore.mapper.MemberMapper;
import com.example.onlinestore.mapper.PointRecordMapper;
import com.example.onlinestore.mapper.PointRuleMapper;
import com.example.onlinestore.security.JwtTokenUtil;
import com.example.onlinestore.service.MemberService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemberServiceImpl implements MemberService {

    private static final Logger logger = LoggerFactory.getLogger(MemberServiceImpl.class);

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private PointRuleMapper pointRuleMapper;

    @Autowired
    private PointRecordMapper pointRecordMapper;

    @Override
    @Transactional
    public LoginResponse login(@Valid LoginRequest request) {
        MemberEntity user = memberMapper.findByName(request.getUsername());
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            logger.error("login failed. because username or password is invalid. username:{}, requestPassword:{}", request.getUsername(), request.getPassword());
            throw new BizException(ErrorCode.MEMBER_PASSWORD_INCORRECT);
        }

        String token = jwtTokenUtil.generateToken(new User(user.getName(), user.getPassword(), new ArrayList<>()));
        return new LoginResponse(token);
    }

    @Override
    public Member registry(@Valid MemberRegistryRequest request) {
        // 判断用户名是否重复
        if (memberMapper.findByName(request.getName()) != null) {
            throw new BizException(ErrorCode.MEMBER_EXISTED, request.getName());
        }

        LocalDateTime now = LocalDateTime.now();
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setName(request.getName());
        memberEntity.setPassword(passwordEncoder.encode(request.getPassword()));
        memberEntity.setNickName(request.getNickName());
        memberEntity.setPhone(request.getPhone());
        memberEntity.setGender(request.getGender().name());
        memberEntity.setAge(request.getAge());
        memberEntity.setCreatedAt(now);
        memberEntity.setUpdatedAt(now);

        int effectRows = memberMapper.insertMember(memberEntity);
        if (effectRows != 1) {
            logger.error("insert member failed. because effect rows is 0. memberName:{}", request.getName());
            throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        return memberEntity.toMember();
    }

    @Override
    public Member getMemberById(@NotNull Long id) {
        MemberEntity memberEntity = memberMapper.findById(id);
        if (memberEntity != null) {
            return memberEntity.toMember();
        }
        throw new BizException(ErrorCode.MEMBER_NOT_FOUND, id);
    }

    @Override
    public Member getMemberByName(@NotNull String name) {
        MemberEntity memberEntity = memberMapper.findByName(StringUtils.trim(name));
        if (memberEntity != null) {
            return memberEntity.toMember();
        }
        logger.info("member not found. memberName:{}", name);
        return null;
    }

    @Override
    public Member getLoginMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            if (StringUtils.isBlank(currentUserName)) {
                throw new BizException(ErrorCode.MEMBER_NOT_LOGIN);
            }

            Member member = getMemberByName(currentUserName);
            if (member == null) {
                throw new BizException(ErrorCode.MEMBER_NOT_LOGIN);
            }
            return member;
        }

        throw new BizException(ErrorCode.MEMBER_NOT_LOGIN);

    }

    @Override
    @Transactional
    public Address addAddress(@NotNull @Valid AddressRequest request) {
        LocalDateTime now = LocalDateTime.now();
        AddressEntity address = new AddressEntity();
        address.setMemberId(request.getMemberId());
        address.setReceiverName(request.getReceiverName());
        address.setReceiverPhone(request.getReceiverPhone());
        address.setProvince(request.getProvince());
        address.setCity(request.getCity());
        address.setDistrict(request.getDistrict());
        address.setDetailAddress(request.getDetailAddress());
        address.setCreatedAt(now);
        address.setUpdatedAt(now);

        // 如果是第一个地址，自动设置为默认地址
        List<AddressEntity> existingAddresses = addressMapper.findByMemberId(request.getMemberId());
        if (existingAddresses.isEmpty()) {
            address.setIsDefault(true);
        }

        int effectRow = addressMapper.insert(address);
        if (effectRow != 1) {
            logger.error("insert address failed. because effect rows is 0. memberId:{}", request.getMemberId());
            throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return convertToAddress(address);
    }

    @Override
    @Transactional
    public Address updateAddress(@NotNull @Min(value = 1, message = "地址ID不能小于1") Long id, @NotNull @Valid AddressRequest request) {
        AddressEntity address = addressMapper.findByIdAndMemberId(id, request.getMemberId());
        if (address == null) {
            logger.error("update address failed. because address not found. addressId:{}", id);
            throw new BizException(ErrorCode.ADDRESS_NOT_FOUND, id);
        }


        address.setReceiverName(request.getReceiverName());
        address.setReceiverPhone(request.getReceiverPhone());
        address.setProvince(request.getProvince());
        address.setCity(request.getCity());
        address.setDistrict(request.getDistrict());
        address.setDetailAddress(request.getDetailAddress());
        address.setUpdatedAt(LocalDateTime.now());

        if (request.getIsDefault()) {
            // 如果设置为默认地址，需要更新其他地址为非默认
            int effectRow = addressMapper.updateDefaultStatus(request.getMemberId(), id);
            if (effectRow != 1) {
                logger.error("update address failed. because effect rows is 0. addressId:{}", id);
                throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
            }
        }

        int effectRow = addressMapper.update(address);
        if (effectRow < 1) {
            logger.error("update address failed. because effect rows is 0. addressId:{}", id);
            throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return convertToAddress(address);
    }

    @Override
    @Transactional
    public void deleteAddress(@NotNull @Min(value = 1, message = "地址ID不能小于1") Long id, @NotNull @Min(value = 1, message = "会员ID不能小于1") Long memberId) {
        AddressEntity address = addressMapper.findByIdAndMemberId(id, memberId);
        if (address == null) {
            logger.error("delete address failed. because address not found. addressId:{}", id);
            throw new BizException(ErrorCode.ADDRESS_NOT_FOUND, id);
        }
        addressMapper.delete(id);

        // 如果删除的是默认地址，则设置最新添加的地址为默认地址
        if (address.getIsDefault()) {
            List<AddressEntity> addresses = addressMapper.findByMemberId(memberId);
            if (!addresses.isEmpty()) {
                AddressEntity newDefault = addresses.get(0);
                addressMapper.updateDefaultStatus(memberId, newDefault.getId());
            }
        }
    }

    @Override
    public List<Address> getAddressesByMemberId(@NotNull @Min(value = 1, message = "会员ID不能小于1") Long memberId) {
        List<AddressEntity> addresses = addressMapper.findByMemberId(memberId);
        if (addresses.isEmpty()) {
            return Collections.emptyList();
        }
        return addresses.stream().map(this::convertToAddress).collect(Collectors.toList());
    }

    @Override
    public Address getDefaultAddress(@NotNull @Min(value = 1, message = "会员ID不能小于1") Long memberId) {
        AddressEntity address = addressMapper.findDefaultByMemberId(memberId);
        if (address == null) {
            logger.error("get default address failed. because address not found. memberId:{}", memberId);
            throw new BizException(ErrorCode.MEMBER_DEFAULT_ADDRESS_NOT_FOUND, memberId);
        }
        return convertToAddress(address);
    }

    @Override
    @Transactional
    public void setDefaultAddress(@NotNull @Min(value = 1, message = "地址ID不能小于1") Long addressId, @NotNull @Min(value = 1, message = "会员ID不能小于1") Long memberId) {
        AddressEntity address = addressMapper.findByIdAndMemberId(addressId, memberId);
        if (address == null) {
            logger.error("set default address failed. because address not found. addressId:{}, memberId:{}", addressId, memberId);
            throw new BizException(ErrorCode.ADDRESS_NOT_FOUND, addressId);
        }
        addressMapper.updateDefaultStatus(memberId, addressId);
    }

    @Override
    public Address getAddressById(@NotNull @Min(value = 1, message = "地址ID不能小于1") Long id) {
        AddressEntity address = addressMapper.findById(id);
        if (address == null) {
            logger.error("get address failed. because address not found. addressId:{}", id);
            throw new BizException(ErrorCode.ADDRESS_NOT_FOUND, id);
        }
        return convertToAddress(address);
    }

    private Address convertToAddress(AddressEntity addressEntity) {
        if (addressEntity == null) {
            return null;
        }
        Address address = new Address();
        address.setId(addressEntity.getId());
        address.setMemberId(addressEntity.getMemberId());
        address.setReceiverName(addressEntity.getReceiverName());
        address.setReceiverPhone(addressEntity.getReceiverPhone());
        address.setProvince(addressEntity.getProvince());
        address.setCity(addressEntity.getCity());
        address.setDistrict(addressEntity.getDistrict());
        address.setDetailAddress(addressEntity.getDetailAddress());
        address.setIsDefault(addressEntity.getIsDefault());
        return address;
    }

    @Override
    @Transactional
    public PointRule createRule(@NotNull @Size(max = 64, message = "规则名称长度不能超过64个字符") String name,
                                @NotNull @Size(max = 128, message = "规则描述长度不能超过128个字符") String description,
                                @NotNull @DecimalMin(value = "1", message = "积分数量必须大于0") BigDecimal points) {
        PointRuleEntity entity = new PointRuleEntity();
        entity.setName(name);
        entity.setDescription(description);
        entity.setPoints(points);
        entity.setStatus(PointRuleStatus.ENABLE.name());
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        int effectRows = pointRuleMapper.insert(entity);
        if (effectRows != 1) {
            logger.error("Failed to create point rule. Effect rows: {}", effectRows);
            throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return convertToPointRule(entity);
    }

    @Override
    @Transactional
    public void updateRuleStatus(@NotNull @Min(value = 1, message = "规则ID必须大于0") Long ruleId, @NotNull PointRuleStatus status) {
        PointRuleEntity ruleEntity = pointRuleMapper.findById(ruleId);
        if (ruleEntity == null) {
            logger.error("Point rule not found. Rule ID: {}", ruleId);
            throw new BizException(ErrorCode.POINT_RULE_NOT_FOUND, ruleId);
        }
        int effectRows = pointRuleMapper.updateStatus(ruleId, status.name());
        if (effectRows != 1) {
            logger.error("Failed to update point rule status. Effect rows: {}", effectRows);
            throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public BigDecimal getMemberPointBalance(@NotNull @Min(value = 1, message = "会员ID必须大于0") Long memberId) {
        return pointRecordMapper.getMemberPointBalance(memberId);
    }

    @Override
    public List<PointRecord> getMemberPointRecords(@NotNull @Min(value = 1, message = "会员ID必须大于0") Long memberId) {
        List<PointRecordEntity> entities = pointRecordMapper.findByMemberId(memberId);
        return entities.stream()
                .map(this::convertToPointRecord)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void earnPoints(@NotNull @Min(value = 1, message = "会员ID必须大于0") Long memberId,
                           @NotNull @Min(value = 1, message = "订单ID必须大于0") Long orderId,
                           @NotNull @DecimalMin(value = "1", message = "积分数量必须大于0") BigDecimal points,
                           @NotNull @Size(max = 128, message = "描述长度不能超过128个字符") String description) {
        PointRecordEntity entity = new PointRecordEntity();
        entity.setMemberId(memberId);
        entity.setOrderId(orderId);
        entity.setPoints(points);
        entity.setType(PointRecordType.EARN.name());
        entity.setDescription(description);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        int effectRows = pointRecordMapper.insert(entity);
        if (effectRows != 1) {
            logger.error("Failed to earn points. Effect rows: {}", effectRows);
            throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional
    public void consumePoints(@NotNull @Min(value = 1, message = "会员ID必须大于0") Long memberId,
                              @NotNull @Min(value = 1, message = "订单ID必须大于0") Long orderId,
                              @NotNull @DecimalMin(value = "1", message = "积分数量必须大于0") BigDecimal points,
                              @NotNull @Size(max = 128, message = "描述长度不能超过128个字符") String description) {
        // 检查积分余额是否足够
        BigDecimal balance = getMemberPointBalance(memberId);
        if (balance.compareTo(points) < 0) {
            throw new BizException(ErrorCode.POINT_BALANCE_INSUFFICIENT, memberId);
        }

        PointRecordEntity entity = new PointRecordEntity();
        entity.setMemberId(memberId);
        entity.setOrderId(orderId);
        entity.setPoints(points);
        entity.setType(PointRecordType.CONSUME.name());
        entity.setDescription(description);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

       int effectRows = pointRecordMapper.insert(entity);
        if (effectRows != 1) {
            logger.error("Failed to consume points. Effect rows: {}", effectRows);
            throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private PointRule convertToPointRule(PointRuleEntity entity) {
        if (entity == null) {
            return null;
        }
        PointRule pointRule = new PointRule();
        pointRule.setId(entity.getId());
        pointRule.setName(entity.getName());
        pointRule.setDescription(entity.getDescription());
        pointRule.setPoints(entity.getPoints());
        pointRule.setStatus(PointRuleStatus.valueOf(entity.getStatus()));
        return pointRule;
    }

    private PointRecord convertToPointRecord(PointRecordEntity entity) {
        if (entity == null) {
            return null;
        }
        PointRecord record = new PointRecord();
        record.setId(entity.getId());
        record.setMemberId(entity.getMemberId());
        record.setOrderId(entity.getOrderId());
        record.setPoints(entity.getPoints());
        record.setType(PointRecordType.valueOf(entity.getType()));
        record.setDescription(entity.getDescription());
        return record;
    }
}