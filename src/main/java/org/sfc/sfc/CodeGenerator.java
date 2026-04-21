package org.sfc.sfc;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;

public class CodeGenerator
{
    // ====================== 只需要改这里 ======================
    private static final String URL = "jdbc:mysql://localhost:3306/suxy?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Su@251485";
    private static final String TABLE_NAME = "t_sys_user,t_sys_permission,t_sys_role_permission";
    private static final String PACKAGE = "org.sfc.sfc"; // 包名
    // =========================================================

    public static void main(String[] args) {
        FastAutoGenerator.create(URL, USERNAME, PASSWORD)
                .globalConfig(builder -> {
                    builder.outputDir(System.getProperty("user.dir") + "/src/main/java")
                            .author("nfc")
                            .disableOpenDir();
                })
                .packageConfig(builder -> {
                    builder.parent(PACKAGE)
                            .entity("entity")
                            .mapper("mapper")
                            .service("service")
                            .serviceImpl("service.impl")
                            .controller("controller");
                })
                .strategyConfig(builder -> {
                    builder.addInclude(TABLE_NAME)
                            .addTablePrefix("t_")
                            .entityBuilder()
                            .enableLombok()
                            .enableTableFieldAnnotation()

                            .controllerBuilder()


                            .serviceBuilder()
                            .formatServiceFileName("%sService")
                            .formatServiceImplFileName("%sServiceImpl");
                })
                .templateEngine(new VelocityTemplateEngine())
                .execute();
    }
}
