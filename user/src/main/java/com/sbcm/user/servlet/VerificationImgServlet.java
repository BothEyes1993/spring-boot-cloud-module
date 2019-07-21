package com.sbcm.user.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.sbcm.common.utils.RandomGraphic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


//告诉编译器忽略指定的警告
@SuppressWarnings("serial")

//@WebServlet定义servlet的注解，urlPatterns设置servlet地址
@WebServlet(urlPatterns = "/servlet/verificationImgServlet")
public class VerificationImgServlet extends HttpServlet{
    private static final Logger logger = LoggerFactory.getLogger(VerificationImgServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        // 设置输出内容为图像，格式为jpeg
        res.setContentType("image/png");
        try {
            // 将内容输出到响应客户端对象的输出流中，生成的图片中包含6个字符
            String verification = RandomGraphic.createInstance(4).drawAlpha(RandomGraphic.GRAPHIC_PNG, res.getOutputStream());
            // 将字符串的值保留在session中，便于和用户手工输入的验证码比较，比较部分不是本文讨论重点，故略
            /*
             * RequestContext context=RequestContext.getInstance();
             * context..put("verification",verification);
             */
            HttpSession session = req.getSession(false);
            session.setAttribute("verification", verification);
            req.getSession().setAttribute("rv", verification);
        } catch (Exception e) {
            logger.error("VerificationImgServlet异常:{}",e);
        }
    }
}
