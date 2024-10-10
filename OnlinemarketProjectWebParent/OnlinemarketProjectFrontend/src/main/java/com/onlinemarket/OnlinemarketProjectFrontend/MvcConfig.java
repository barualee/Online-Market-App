package com.onlinemarket.OnlinemarketProjectFrontend;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // String dirName = "user-photos";
        // Path userPhotosDir = Paths.get(dirName);
        // String userPhotosPath = userPhotosDir.toFile().getAbsolutePath();
        // registry.addResourceHandler("/"+dirName+"/**").addResourceLocations("file:"+userPhotosPath+"/");

        String categoriesDirName = "category-photos";
        Path categoryPhotosDir = Paths.get(categoriesDirName);
        String categoryPhotosPath = categoryPhotosDir.toFile().getAbsolutePath();
        registry.addResourceHandler("/"+categoriesDirName+"/**").addResourceLocations("file:"+categoryPhotosPath+"/");

        String brandsDirName = "brands-logos";
        Path brandLogosDir = Paths.get(brandsDirName);
        String brandLogosPath = brandLogosDir.toFile().getAbsolutePath();
        registry.addResourceHandler("/"+brandsDirName+"/**").addResourceLocations("file:"+brandLogosPath+"/");
	}

}

