package domain.helpers;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class UrlHelper {

    public static void appendQueryParam(StringBuilder url, String paramName, Object value) {
        if (value != null && !value.toString().isEmpty()) {
            if (url.indexOf("?") == -1) {
                url.append("?");
            } else {
                url.append("&");
            }
            url.append(paramName)
                    .append("=")
                    .append(URLEncoder.encode(value.toString(), StandardCharsets.UTF_8));
        }
    }

    public static void appendQueryParamSinEncode(StringBuilder url, String paramName, Object value) {
        if (value != null && !value.toString().isEmpty()) {
            if (url.indexOf("?") == -1) {
                url.append("?");
            } else {
                url.append("&");
            }
            url.append(paramName)
                    .append("=")
                    .append(value.toString());
        }
    }

    public static String appendAllQueryParams(StringBuilder url, String... params) {
        if (params == null || params.length == 0) {
            return url.toString();
        }

        boolean first = !url.toString().contains("?");

        for (String param : params) {
            if (param != null && !param.isEmpty()) {
                url.append(first ? "?" : "&");
                url.append(URLEncoder.encode(param, StandardCharsets.UTF_8));
                first = false;
            }
        }

        return url.toString();
    }

    public static void appendHeaderParam(StringBuilder url, String headerName, String headerValue) {
        if (headerValue != null && !headerValue.isEmpty()) {
            if (url.indexOf("?") == -1) {
                url.append("?");
            } else {
                url.append("&");
            }
            url.append("header_")
                    .append(headerName)
                    .append("=")
                    .append(URLEncoder.encode(headerValue, StandardCharsets.UTF_8));
        }
    }
}