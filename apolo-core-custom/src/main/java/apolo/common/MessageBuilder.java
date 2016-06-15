package apolo.common;

import org.springframework.stereotype.Component;

@Component
public class MessageBuilder {

    public String buildAuthenticationMessage(
            String tenantName,
            String subject,
            String message
    ) {
        StringBuilder result = new StringBuilder();

        result.append("<html>");
        result.append("<body>");

        result.append("<h1>");
        result.append(tenantName + ": " + subject);
        result.append("</h1>");

        result.append("<p>");
        result.append(message);
        result.append("</p>");

        result.append("</body>");
        result.append("</html>");

        return result.toString();
    }

    public String buildResetPasswordMessage(
            String url,
            String subject,
            String message,
            String token,
            String footer
    ) {
        StringBuilder result = new StringBuilder();

        result.append("<html>");
        result.append("<body>");

        result.append("<h1>");
        result.append(subject);
        result.append("</h1>");

        result.append("<p>");
        result.append(message);
        result.append("</p>");

        result.append("<p>");

        result.append("<a href=\"" + url + token + "\" >");
        result.append(url + token);
        result.append("</a>");

        result.append("</p>");

        result.append("<p>");
        result.append(footer);
        result.append("</p>");

        result.append("</body>");
        result.append("</html>");

        return result.toString();
    }

    public String buildCreateUserMessage(
            String url,
            String subject,
            String message,
            String token,
            String footer
    ) {
        StringBuilder result = new StringBuilder();

        result.append("<html>");
        result.append("<body>");

        result.append("<h1>");
        result.append(subject);
        result.append("</h1>");

        result.append("<p>");
        result.append(message);
        result.append("</p>");

        result.append("<p>");

        result.append("<a href=\"" + url + token + "\" >");
        result.append(url + token);
        result.append("</a>");

        result.append("</p>");

        result.append("<p>");
        result.append(footer);
        result.append("</p>");

        result.append("</body>");
        result.append("</html>");

        return result.toString();
    }

}
