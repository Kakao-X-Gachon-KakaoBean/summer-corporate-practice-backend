package com.kakaobean.common;

public class EmailHtmlUtils {

    private EmailHtmlUtils() {}


    public static String makeLinkHtml(String header,
                                      String firstLine,
                                      String text1,
                                      String boldKeyword,
                                      String text2,
                                      String boxTitle,
                                      String url
    ) {
        StringBuilder emailContent = new StringBuilder();
        emailContent.append("<!DOCTYPE html>");
        emailContent.append("<html>");
        emailContent.append("<head>");
        emailContent.append("</head>");
        emailContent.append("<body>");
        emailContent.append(
                " <div" +
                        "	style=\"font-family: 'Apple SD Gothic Neo', 'sans-serif' !important; width: 700px; height: 900px; border-top: 4px solid #29ABE2; margin: 100px auto; padding: 30px 0; box-sizing: border-box;\">" +
                        "	<h1 style=\"margin: 0; padding: 0 5px; font-size: 28px; font-weight: 400;\">" +
                        "		<span style=\"font-size: 50px; margin: 0 0 10px 3px;\">코코노트</span><br/>" +
                        "		<span style=\"color: #29ABE2\">" + header + "</span> 안내입니다." +
                        "	</h1>\n" +
                        "	<p style=\"font-size: 16px; line-height: 26px; margin-top: 50px; padding: 0 5px;\">" + firstLine +
                        "	    <br/> " +
                        text1 + " <strong style=\"color:#29ABE2\">" + boldKeyword + " </strong>" + text2   + "<br/>" +
                        "  <a href=\"http://" + url + "\" style=\"text-decoration: none;\">" +
                        "      <div style=\"width: 576px;height: 90px; margin-top: 50px; padding: 0 27px;color: #242424;font-size: 16px;font-weight: bold;background-color: #F9F9F9;vertical-align: middle;line-height: 90px;\"> "+ boxTitle + " : " +
                        "          <strong style=\"font-style: normal;font-weight: bold;color: #29ABE2\"> " + url + "</strong>" +
                        "      </div>" +
                        "  </a>" +
                        "	<p style=\"font-size: 16px; line-height: 26px; margin-top: 50px; padding: 0 5px;\">" +
                        "	    <br/>" +
                        "		감사합니다.<br/>" +
                        "	<div style=\"border-top: 4px solid #29ABE2; margin: 40px auto; padding: 30px 0;\"></div>" +
                        " </div>");
        emailContent.append("</body>");
        emailContent.append("</html>");
        String content = emailContent.toString();
        return content;
    }

    public static String getBoardHtml(String header,
                                      String firstLine,
                                      String text1,
                                      String boldKeyword,
                                      String text2,
                                      String boxTitle,
                                      String boxContent) {
        StringBuilder emailContent = new StringBuilder();
        emailContent.append("<!DOCTYPE html>");
        emailContent.append("<html>");
        emailContent.append("<head>");
        emailContent.append("</head>");
        emailContent.append("<body>");
        emailContent.append(
                " <div" +
                        "	style=\"font-family: 'Apple SD Gothic Neo', 'sans-serif' !important; width: 700px; height: 900px; border-top: 4px solid #29ABE2; margin: 100px auto; padding: 30px 0; box-sizing: border-box;\">" +
                        "	<h1 style=\"margin: 0; padding: 0 5px; font-size: 28px; font-weight: 400;\">" +
                        "		<span style=\"font-size: 50px; margin: 0 0 10px 3px;\">코코노트</span><br/>" +
                        "		<span style=\"color: #29ABE2\">" + header + "</span> 안내입니다." +
                        "	</h1>\n" +
                        "	<p style=\"font-size: 16px; line-height: 26px; margin-top: 50px; padding: 0 5px;\">" + firstLine +
                        "	    <br/> " +
                        text1 + " <strong style=\"color:#29ABE2\">" + boldKeyword + " </strong>" + text2   + "<br/>" +
                        "   <div style=\"width: 576px;height: 90px; margin-top: 50px; padding: 0 27px;color: #242424;font-size: 16px;font-weight: bold;background-color: #F9F9F9;vertical-align: middle;line-height: 90px;\"> " + boxTitle +
                        " <strong style=\"font-style: normal;font-weight: bold;color: #29ABE2\">" + boxContent + "</strong></div>" +
                        "	<p style=\"font-size: 16px; line-height: 26px; margin-top: 50px; padding: 0 5px;\">" +
                        "	    <br/>" +
                        "		감사합니다.<br/>" +
                        "	<div style=\"border-top: 4px solid #29ABE2; margin: 40px auto; padding: 30px 0;\"></div>" +
                        " </div>");
        emailContent.append("</body>");
        emailContent.append("</html>");
        String content = emailContent.toString();
        return content;
    }
}