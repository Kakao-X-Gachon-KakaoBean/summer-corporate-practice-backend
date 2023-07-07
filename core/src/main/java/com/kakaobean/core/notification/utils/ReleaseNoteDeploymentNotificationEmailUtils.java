package com.kakaobean.core.notification.utils;

public class ReleaseNoteDeploymentNotificationEmailUtils {

    public static String getHtml(String releaseNoteTitle, String url) {
        StringBuilder emailContent = new StringBuilder();
        emailContent.append("<!DOCTYPE html>");
        emailContent.append("<html>");
        emailContent.append("<head>");
        emailContent.append("</head>");
        emailContent.append("<body>");
        emailContent.append(
                " <div style=\"font-family: 'Apple SD Gothic Neo', 'sans-serif' !important; width: 700px; height: 900px; border-top: 4px solid #29ABE2; margin: 100px auto; padding: 30px 0; box-sizing: border-box;\">" +
                        "	<h1 style=\"margin: 0; padding: 0 5px; font-size: 28px; font-weight: 400;\">" +
                        "		<span style=\"font-size: 50px; margin: 0 0 10px 3px;\">코코노트</span><br/>" +
                        "		<span style=\"color: #29ABE2\">프로젝트 초대 </span> 안내입니다." +
                        "	</h1>\n" +
                        "	<p style=\"font-size: 16px; line-height: 26px; margin-top: 50px; padding: 0 5px;\">" +
                        "	    프로젝트 참여를 위한 링크를 알려드립니다. <br/> 아래 링크로 " + "<strong style=\"color:#29ABE2\"> </strong> 입장하시면 프로젝트에 참여 가능합니다. <br/>" +
                        "   </p> "+
                        "  <a href=\"http://" + url + "\" style=\"text-decoration: none;\">" +
                        "      <div style=\"width: 576px;height: 90px; margin-top: 50px; padding: 0 27px;color: #242424;font-size: 16px;font-weight: bold;background-color: #F9F9F9;vertical-align: middle;line-height: 90px;\">초대 링크 : " +
                        "          <strong style=\"font-style: normal;font-weight: bold;color: #29ABE2\"> " + url + "</strong>" +
                        "      </div>" +
                        "  </a>" +
                        "	<p style=\"font-size: 16px; line-height: 26px; margin-top: 50px; padding: 0 5px;\">" +
                        "	    <br/> <br/>" +
                        "		감사합니다.<br/>" +
                        "	<div style=\"border-top: 4px solid #29ABE2; margin: 40px auto; padding: 30px 0;\"></div>" +
                        " </div>");
        emailContent.append("</body>");
        emailContent.append("</html>");
        String content = emailContent.toString();
        return content;
    }
}
