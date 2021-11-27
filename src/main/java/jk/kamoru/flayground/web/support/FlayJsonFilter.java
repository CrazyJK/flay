package jk.kamoru.flayground.web.support;

import java.util.List;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import jk.kamoru.flayground.web.FlaygroundController.Faram.FIELD;

/**
 * 객체를 json으로 변환시 객체 필드 선택
 */
public class FlayJsonFilter extends SimpleBeanPropertyFilter {

	private List<FIELD> fields;

	public FlayJsonFilter(List<FIELD> fields) {
		this.fields = fields;
	}

	@Override
	protected boolean include(BeanPropertyWriter writer) {
		return check(writer.getName());
	}

	@Override
	protected boolean include(PropertyWriter writer) {
		return check(writer.getName());
	}

	private boolean check(String name) {
		if (fields == null || fields.size() == 0) {
			return true;
		}

		return fields.stream().anyMatch(f -> f.name().equalsIgnoreCase(name));
	}

}
