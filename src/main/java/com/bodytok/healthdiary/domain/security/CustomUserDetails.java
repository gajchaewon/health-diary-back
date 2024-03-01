package com.bodytok.healthdiary.domain.security;

import com.bodytok.healthdiary.domain.UserAccount;
import com.bodytok.healthdiary.dto.UserAccountDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

    private Long id;
    private String email;
    private String nickname;
    private String userPassword;
    private Byte[] profileImage;

    private Collection<? extends GrantedAuthority> authorities;


    private static final Set<RoleType> roleTypes = Set.of(RoleType.USER);

    public static CustomUserDetails of(String email,String nickname,String userPassword, Byte[] profileImage) {
        return new CustomUserDetails(
                null,
                email,
                nickname,
                userPassword,
                profileImage,
                roleTypes.stream()
                        .map(RoleType::getName)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toUnmodifiableSet())
        );
    }

    public static CustomUserDetails of(Long id, String email,String nickname,String userPassword, Byte[] profileImage) {
        return new CustomUserDetails(
                id,
                email,
                nickname,
                userPassword,
                profileImage,
                roleTypes.stream()
                        .map(RoleType::getName)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toUnmodifiableSet())
        );
    }

    public static CustomUserDetails from(UserAccountDto dto) {
        return CustomUserDetails.of(
                dto.id(),
                dto.email(),
                dto.nickname(),
                dto.userPassword(),
                dto.profileImage()
        );
    }

    public static UserAccount toUserEntity(CustomUserDetails userDetails){
        return UserAccount.of(
                userDetails.getEmail(),
                userDetails.getNickname(),
                userDetails.getPassword(),
                userDetails.getProfileImage()
        );
    }
    public UserAccountDto toDto() {
        return UserAccountDto.of(
                id,
                email,
                nickname,
                userPassword,
                profileImage
        );
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return userPassword;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Getter
    public enum RoleType {
        USER("ROLE_USER");

        private final String name;

        RoleType(String name){
            this.name = name;
        }
    }
}
