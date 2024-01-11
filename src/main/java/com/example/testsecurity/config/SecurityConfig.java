package com.example.testsecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // WebSecurity를 설정하는 파일로 지정
public class SecurityConfig {

    /**
     * //== SecurityFilterChain ==//
     * SecurityFilterChain 인터페이스를 사용해서
     * 새로운 FilterChain을 만들어준다
     */

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        /**
         * //== requestMatcher ==//
         * requestMatcher를 사용함에 있어서 순서도 고려해야한다.
         * 만약 위에 모든 사용자에게 승인한 것을 특정 Role에게만 적용시킨다면
         * 적용이 안될 수 있다.
         */
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/","/login","/loginProc", "/join", "/joinProc").permitAll()
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .requestMatchers("/my/**").hasAnyRole("ADMIN", "USER")
                        .anyRequest().authenticated()
                );

        /**
         * //== formLogin ==//
         * formLogin을 통해서 아래와 같은
         * loginPage와 loginProcessingUrl을 설정해준다면
         * 인증 또는 인가가 필요한 Request가 인증되지 않았다면, loginPage를 반환하도록 만들어준다.
         * -> HttpBasic 사용
         */
//        http
//                .formLogin(auth -> auth
//                        .loginPage("/login")
//                        .loginProcessingUrl("/loginProc")
//                        .permitAll()
//                );

        /**
         * //== csrf ==//
         * 여기서 csrf는 사이트 위/변조 공격을 말한다.
         * csrf에 대한 방어 기능을 해제하는 것이다.
         * Spring Security에서는 POST요청을 보낼 때 csrf 토큰을 같이 보내야 한다.
         * csrf.disable을 설정하지 않으면 자동으로 enable설정으로 진행한다.
         * CsrfFilter를 통해서 POST, PUT, DELETE 요청에 대해서 토큰 검증 한다.
         * ** API 서버의 경우에느 보통 세션을 STATELESS로 관리하기 때문에
         * 스프링 시큐리티 csrf enable 설정을 진행하지 않아도 된다.
         * -> HttpBasic에서 사용
         */
        http.
                csrf(auth -> auth.disable());

        /**
         * //== sessionManagement ==//
         * maximumSessions -> 하나의 아이디에 대한 다중 로그인 허용 개수
         * maxSessionsPreventsLogin -> 다중 로그인 개수를 초과했을 경우 처리 방법
         *      - true : 초과시 새로운 로그인 차단
         *      - false : 초과시 기존 세션 하나 삭제
         * 세션 고정 보호
         *
         */
//        http
//                .sessionManagement(auth -> auth
//                        .maximumSessions(1)
//                        .maxSessionsPreventsLogin(true)
//                );
//
//        http
//                .sessionManagement(auth -> auth
//                        .sessionFixation().changeSessionId()
//                );

        /**
         * //== logout ==//
         * csrf 설정시 POST 요청으로 로그아웃해야 하지만,
         * 아래 방식으로 GET 방식으로 진행 할 수 있다
         * 설정 하지 않으면, url을 통해서 logout할 수 없다.
         */
//        http
//                .logout(auth -> auth
//                        .logoutUrl("/logout")
//                        .logoutSuccessUrl("/")
//                );

        /**
         * //== HttpBasic ==//
         * 아이디와 비밀번호를 Base64로 인코딩한 뒤에 HTTP 인증 헤더에 부착하여 서버측으로 요청을 보내는 방식이다.
         * 일반적으로 MSA구조를 구축할 때에 쓰는 유레카/Config 서버에서 더 엄격한 보안을 요구하기 위해서 사용한다.
         */
        http.httpBasic(Customizer.withDefaults());

        return http.build();
    }

    /**
     * Spring Security 는 사용자 인증시 비밀번호에 대해 단뱡향 해시 암호화를 진행해
     * DB에 저장된 비밀번호와 대조한다.
     *
     * Spring Security는 BCryptPasswordEncoder를 제공하고 권장한다.
     *
     * -암호화의 종류
     *      - 양방향
     *          - 대칭키
     *          - 비대칭키
     *      - 단뱡향
     *          - 해시
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * //== InMemoryUserDetailsManager ==//
     * 소수의 유저를 저장할 좋은 방법
     * 토이 프로젝트를 진행하는 경우 또는 시큐리티 로그인 환경이 필요하지만
     * 소수의 회원 정보만 가지며 데이터베이스라는 자원을 투자하기 힘든 경우는 회원가입 없는 InMemory 방식으로 유저를 저장하면 된다.
     * 이 경우 InMemoryUserDetailsManager 클래스를 통해 유저를 등록하면 된다.
     */
//    @Bean
//    public UserDetailsService userDetailsService() {
//
//        UserDetails user1 = User.builder()
//                .username("user1")
//                .password(bCryptPasswordEncoder().encode("1234"))
//                .roles("ADMIN")
//                .build();
//
//        UserDetails user2 = User.builder()
//                .username("user2")
//                .password(bCryptPasswordEncoder().encode("1234"))
//                .roles("USER")
//                .build();
//
//        return new InMemoryUserDetailsManager(user1, user2);
//    }

    /**
     * //== Role Hierarchy (계층 권한)
     * 아래의 코드와 같이 A < B < C 순의 권한 계층을 가지게 된다면
     * requestMatchers().hasRole()에서 A에게만 권한을 줘도 B, C 권한이
     * 더 상위 권한이기에 B, C 또한, 접근 가능하다.
     */
    @Bean
    public RoleHierarchy roleHierarchy() {

        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();

        hierarchy.setHierarchy("ROLE_C > ROLE_B\n" +
                "ROLE_B > ROLE_A");

        return hierarchy;
    }
}
