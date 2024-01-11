# Spring Security 3.1.7 Example Repository
이 Repository는 Spring Security 3.1.7 버전을 기반으로한 기본적인 보안 설정 및 활용 방법을 다룹니다.

---
Security 설정
Spring Security에서의 핵심 설정은 SecurityFilterChain을 구성하는 것입니다. 
다양한 보안 기능을 설정해보고 다음과 같은 구성을 공부해봤습니다.
스프링은 버전에 따라 구현 방식이 변경되는데
시큐리티의 경우 특히 세부 버전별로 구현 방법이 많이 다르기 때문에 버전 마다 구현 특징을 확인해야 한다.
[Spring Security Reference](https://docs.spring.io/spring-security/reference/6.1-SNAPSHOT/index.html)


- SecurityFilterChain 구성
  - authorizeHttpRequests: HTTP 요청에 대한 권한 설정
  - formLogin: 폼 기반 로그인 설정
  - csrf: CSRF(Cross-Site Request Forgery) 보안 설정
  - sessionManagement: 세션 관리 설정
  - logout: 로그아웃 설정
  - httpBasic: HTTP Basic 인증 설정
  
- Password 인코딩 : BCryptPasswordEncoder 사용
- RoleHierarchy 설정
- InMemoryUserDetailsManager 설정

- UserDetails 및 UserDetailsService 구현
  - UserDetails 구현
  - UserDetailsService를 구현
  - UserDetails와 UserDetailsService를 이용한 인증/인가 방식
