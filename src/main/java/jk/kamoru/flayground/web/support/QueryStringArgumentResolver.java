package jk.kamoru.flayground.web.support;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import javax.servlet.http.HttpServletRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class QueryStringArgumentResolver implements HandlerMethodArgumentResolver {

	private static final char CEMICOLON = ':';
	private static final char COMMA = ',';

	@Autowired ObjectMapper mapper;

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterAnnotation(QueryStringArgResolver.class) != null;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		final HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
		final String json = queryStringToJson(request.getQueryString());
		final Object paramObject = mapper.readValue(json, parameter.getParameterType());
		return paramObject;
	}

	private String queryStringToJson(String queryString) throws JsonProcessingException {
		if (queryString == null) {
			return "{}";
		}
		String decodedQueryString = URLDecoder.decode(queryString, StandardCharsets.UTF_8);
		log.debug("queryString [{}]", decodedQueryString);

		int count = 0;
		String[] pairs = decodedQueryString.split("&");
		StringBuilder sb = new StringBuilder("{");
		for (String pair : pairs) {
			int eqPos = pair.indexOf("=");
			String key = null;
			String val = null;
			if (eqPos == -1) {
				key = pair;
				val = null;
			} else if (eqPos == 0) {
				continue;
			} else {
				key = pair.substring(0, eqPos);
				val = pair.substring(eqPos + 1);
			}
			if (count > 0) {
				sb.append(COMMA);
			}
			sb.append(mapper.writeValueAsString(key));
			sb.append(CEMICOLON);
			sb.append(mapper.writeValueAsString(val));
			count++;
		}
		sb.append("}");
		log.debug(" jsonString [{}]", sb.toString());
		return sb.toString();
	}

}
