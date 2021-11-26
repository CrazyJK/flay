package jk.kamoru.flayground.lang;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

public class LangTest {

	@Test
	public void splitTest() {
		String historyLine = "2021-11-25 22:27:21, DLDSS-025, PLAY, [HAHLIA][DLDSS-025][항상 페니 핥고 싶다. 직장의 음란 미인][Mino Suzume][2021.09.09]";
		String[] split = StringUtils.split(historyLine, ",", 4);
		assert split.length == 4;
		assert split[0].equals("2021-11-25 22:27:21");
		assert split[1].equals(" DLDSS-025");
		assert split[2].equals(" PLAY");
		assert split[3].equals(" [HAHLIA][DLDSS-025][항상 페니 핥고 싶다. 직장의 음란 미인][Mino Suzume][2021.09.09]");
	}
}
