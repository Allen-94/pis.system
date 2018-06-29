package com.yuyisz.pis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

import com.yuyisz.pis.ui.util.ApplicationStartup;

@SpringBootApplication
@EnableWebSocket
@EnableCaching
public class Application {

	@Bean
	public EmbeddedServletContainerCustomizer containerCustomizer() {

	    return new EmbeddedServletContainerCustomizer() {

			@Override
			public void customize(ConfigurableEmbeddedServletContainer container) {
				ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/error/page_401.html");
	            ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/error/page_404.html");
	            ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error/page_500.html");
	            container.addErrorPages(error401Page, error404Page, error500Page);
			}
	    };
	}
	
	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(Application.class);
		application.addListeners(new ApplicationStartup());
		application.run(args);
	}
}
