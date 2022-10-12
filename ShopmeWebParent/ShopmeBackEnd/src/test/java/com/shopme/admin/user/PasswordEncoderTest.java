package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


public class PasswordEncoderTest {
@Test
public void testEncoderPassword()
{
	BCryptPasswordEncoder bcyptPassword=new BCryptPasswordEncoder();
	String rawPassword="Kunal123";
	String encodedPassword=bcyptPassword.encode(rawPassword);
	System.out.println(encodedPassword);
	boolean matches=bcyptPassword.matches(rawPassword, encodedPassword);
	assertThat(matches).isTrue();
}
}
