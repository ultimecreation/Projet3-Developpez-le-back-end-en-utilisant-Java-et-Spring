package com.chatop.backend.config;

import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.responses.ApiResponse;

@OpenAPIDefinition
@SecurityScheme(name = "Bearer", description = "JWT token", scheme = "Bearer", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", in = SecuritySchemeIn.HEADER)
@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI baseOpenAPI() {

		// UNAUTHORIZED
		ApiResponse unauthorizedRequestApi = new ApiResponse()
				.content(new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
						new io.swagger.v3.oas.models.media.MediaType().addExamples("default",
								new Example().value("{}"))))
				.description("Unauthorized");

		// AUTHENTICATION
		ApiResponse authSuccessRequestApi = new ApiResponse().content(
				new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
						new io.swagger.v3.oas.models.media.MediaType()
								.addExamples("default", new Example().value(
										"{\"token\": \"some jwt token\"}"))))
				.description("Success");

		ApiResponse registerBadRequestApi = new ApiResponse().content(
				new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
						new io.swagger.v3.oas.models.media.MediaType()
								.addExamples("default", new Example().value(
										"{\"name\": \"name is required\",\"email\": \"email is required\",\"password\": \"password is required\"}"))
								.addExamples("email invalid", new Example().value(
										"{\"email\": \"invalid email format\"}"))
								.addExamples("email in use", new Example().value(
										"{\"email\": \"Email already in use\"}"))))
				.description("Bad Request ( 3 examples )");

		ApiResponse loginBadRequestApi = new ApiResponse().content(
				new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
						new io.swagger.v3.oas.models.media.MediaType()
								.addExamples("default",
										new Example().value(
												"{\"email\": \"email is required\",\"password\": \"password is required\"}"))
								.addExamples("email invalid",
										new Example().value(
												"{\"email\": \"invalid email format\"}"))))
				.description("Bad Request ( 2 examples )");

		// ME
		ApiResponse meSuccessRequestApi = new ApiResponse().content(
				new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
						new io.swagger.v3.oas.models.media.MediaType()
								.addExamples("default",
										new Example().value(
												"{\"id\": 1,\"name\": \"Test TEST\",\"email\": \"test@test.com\",\"created_at\": \"2022/02/02\",\"updated_at\": \"2022/08/02\"}"))))
				.addHeaderObject("Authorization", new Header().description("Bearer token"))
				.description("Success");

		// MESSAGES
		ApiResponse messageSuccessRequestApi = new ApiResponse().content(
				new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
						new io.swagger.v3.oas.models.media.MediaType().addExamples("default",
								new Example().value(
										"{\"message\": \"Message send with success\"}"))))
				.addHeaderObject("Authorization", new Header().description("Bearer token"))
				.description("Success");

		ApiResponse messageBadRequestRequestApi = new ApiResponse().content(
				new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
						new io.swagger.v3.oas.models.media.MediaType().addExamples("default",
								new Example().value(
										"{\"user_id\": \"user id is required\",\"message\": \"message is required\",\"rental_id\": \"rental id is required\"}"))))
				.addHeaderObject("Authorization", new Header().description("Bearer token"))
				.description("Bad Request ");

		// USER
		ApiResponse userSuccessRequestApi = new ApiResponse().content(
				new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
						new io.swagger.v3.oas.models.media.MediaType().addExamples("default",
								new Example().value(
										"{\"id\": 2,\"name\": \"Owner Name\",\"email\": \"test@test.com\",\"created_at\": \"2022/02/02\",\"updated_at\": \"2022/08/02\"}"))))
				.addHeaderObject("Authorization", new Header().description("Bearer token"))
				.description("Success");

		ApiResponse userBadRequestRequestApi = new ApiResponse().content(
				new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
						new io.swagger.v3.oas.models.media.MediaType().addExamples("default",
								new Example().value("{\"id\": \"id is required\"}"))))
				.addHeaderObject("Authorization", new Header().description("Bearer token"))
				.description("Bad Request");

		// RENTALS
		ApiResponse getAllRentalsSuccessRequestApi = new ApiResponse().content(
				new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
						new io.swagger.v3.oas.models.media.MediaType().addExamples("default",
								new Example().value(
										"{\"rentals\": [{\"id\": 1,\"name\": \"test house 1\",\"surface\": 432,\"price\": 300,\"picture\": \"https://blog.technavio.org/wp-content/uploads/2018/12/Online-House-Rental-Sites.jpg\",\"description\": \"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam a lectus eleifend, varius massa ac, mollis tortor. Quisque ipsum nulla, faucibus ac metus a, eleifend efficitur augue. Integer vel pulvinar ipsum. Praesent mollis neque sed sagittis ultricies. Suspendisse congue ligula at justo molestie, eget cursus nulla tincidunt. Pellentesque elementum rhoncus arcu, viverra gravida turpis mattis in. Maecenas tempor elementum lorem vel ultricies. Nam tempus laoreet eros, et viverra libero tincidunt a. Nunc vel nisi vulputate, sodales massa eu, varius erat.\",\"owner_id\": 1,\"created_at\": \"2012/12/02\",\"updated_at\": \"2014/12/02\"},{\"id\": 1,\"name\": \"test house 2\",\"surface\": 154,\"price\": 200,\"picture\": \"https://blog.technavio.org/wp-content/uploads/2018/12/Online-House-Rental-Sites.jpg\",\"description\": \"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam a lectus eleifend, varius massa ac, mollis tortor. Quisque ipsum nulla, faucibus ac metus a, eleifend efficitur augue. Integer vel pulvinar ipsum. Praesent mollis neque sed sagittis ultricies. Suspendisse congue ligula at justo molestie, eget cursus nulla tincidunt. Pellentesque elementum rhoncus arcu, viverra gravida turpis mattis in. Maecenas tempor elementum lorem vel ultricies. Nam tempus laoreet eros, et viverra libero tincidunt a. Nunc vel nisi vulputate, sodales massa eu, varius erat.\",\"owner_id\": 2,\"created_at\": \"2012/12/02\",\"updated_at\": \"2014/12/02\"},{\"id\": 3,\"name\": \"test house 3\",\"surface\": 234,\"price\": 100,\"picture\": \"https://blog.technavio.org/wp-content/uploads/2018/12/Online-House-Rental-Sites.jpg\",\"description\": \"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam a lectus eleifend, varius massa ac, mollis tortor. Quisque ipsum nulla, faucibus ac metus a, eleifend efficitur augue. Integer vel pulvinar ipsum. Praesent mollis neque sed sagittis ultricies. Suspendisse congue ligula at justo molestie, eget cursus nulla tincidunt. Pellentesque elementum rhoncus arcu, viverra gravida turpis mattis in. Maecenas tempor elementum lorem vel ultricies. Nam tempus laoreet eros, et viverra libero tincidunt a. Nunc vel nisi vulputate, sodales massa eu, varius erat.\",\"owner_id\": 1,\"created_at\": \"2012/12/02\",\"updated_at\": \"2014/12/02\"}]}"))))
				.addHeaderObject("Authorization", new Header().description("Bearer token"))
				.description("Success");

		ApiResponse getSingleRentalSuccessRequestApi = new ApiResponse().content(
				new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
						new io.swagger.v3.oas.models.media.MediaType().addExamples("default",
								new Example().value(
										"{\"id\": 1,\"name\": \"test house 1\",\"surface\": 432,\"price\": 300,\"picture\": \"https://blog.technavio.org/wp-content/uploads/2018/12/Online-House-Rental-Sites.jpg\",\"description\": \"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam a lectus eleifend, varius massa ac, mollis tortor. Quisque ipsum nulla, faucibus ac metus a, eleifend efficitur augue. Integer vel pulvinar ipsum. Praesent mollis neque sed sagittis ultricies. Suspendisse congue ligula at justo molestie, eget cursus nulla tincidunt. Pellentesque elementum rhoncus arcu, viverra gravida turpis mattis in. Maecenas tempor elementum lorem vel ultricies. Nam tempus laoreet eros, et viverra libero tincidunt a. Nunc vel nisi vulputate, sodales massa eu, varius erat.\",\"owner_id\": 1,\"created_at\": \"2012/12/02\",\"updated_at\": \"2014/12/02\"}"))))
				.addHeaderObject("Authorization", new Header().description("Bearer token"))
				.description("Success");

		ApiResponse createRentalSuccessRequestApi = new ApiResponse().content(
				new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
						new io.swagger.v3.oas.models.media.MediaType().addExamples("default",
								new Example().value("{\"message\": \"Rental created !\"}"))))
				.addHeaderObject("Authorization", new Header().description("Bearer token"))
				.description("Success");

		ApiResponse updateRentalSuccessRequestApi = new ApiResponse().content(
				new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
						new io.swagger.v3.oas.models.media.MediaType().addExamples("default",
								new Example().value("{\"message\": \"Rental updated !\"}"))))
				.addHeaderObject("Authorization", new Header().description("Bearer token"))
				.description("Success");

		Components components = new Components();
		components.addResponses("unauthorizedRequestApi", unauthorizedRequestApi);

		components.addResponses("authSuccessRequestApi", authSuccessRequestApi);
		components.addResponses("registerBadRequestApi", registerBadRequestApi);
		components.addResponses("loginBadRequestApi", loginBadRequestApi);

		components.addResponses("meSuccessRequestApi", meSuccessRequestApi);

		components.addResponses("messageSuccessRequestApi", messageSuccessRequestApi);
		components.addResponses("messageBadRequestRequestApi", messageBadRequestRequestApi);

		components.addResponses("userSuccessRequestApi", userSuccessRequestApi);
		components.addResponses("userBadRequestRequestApi", userBadRequestRequestApi);

		components.addResponses("getAllRentalsSuccessRequestApi", getAllRentalsSuccessRequestApi);
		components.addResponses("getSingleRentalSuccessRequestApi", getSingleRentalSuccessRequestApi);
		components.addResponses("createRentalSuccessRequestApi", createRentalSuccessRequestApi);
		components.addResponses("updateRentalSuccessRequestApi", updateRentalSuccessRequestApi);

		return new OpenAPI()
				.components(components)
				.info(new Info().title("Chatop Doc").version("1.0.0").description("Chatop Doc"));
	}
}
// @OpenAPIDefinition()
// @SecurityScheme(name = "Bearer", description = "JWT token", scheme =
// "Bearer", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", in =
// SecuritySchemeIn.HEADER

// )
