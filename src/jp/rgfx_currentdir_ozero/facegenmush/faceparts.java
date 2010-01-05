package jp.rgfx_currentdir_ozero.facegenmush;

// 顔パーツ
public class faceparts {
	String fdcolor[][];
	String fdrinkaku[][];
	String fdotete[][];
	String fdomeme[][];
	String fdokuti[][];
	String fdhoppe[][];

	public faceparts(int mode) {
		switch(mode){
		case 0:
			defineparts_original();
			break;
		case 1:
			defineparts_safe();
			break;
		}
		return;
	}

	// 候補定義: オリジナル
	private void defineparts_original() {
		// 候補
		String afdcolor[][] = { { "255", "192", "203" },
				{ "128", "128", "128" }, { "136", "136", "255" } };
		String afdrinkaku[][] = { { "(", ")" }, { "(", ")" }, { "|", "|" },
				{ "[", "]" } };
		String afdotete[][] = {
				{ "", "", "", "", "" },
				{ "", "", "m", "", "" },
				{ "", "", "ლ", "", "" }, // ng 'GEORGIAN LETTER LAS'
											// (U+10DA)
				{ "ლ", "", "", "ლ", "" }, // ng
				{ "", "｢", "", "", "｢" }, { "", " つ", "", "", "つ" },
				{ "", " ", "", "", "o彡ﾟ" }, { "", "n", "", "", "η" },
				{ "", "∩", "", "∩", "" }, { "∩", "", "", "", "∩" },
				{ "ヽ", "", "", "", "ノ" }, { "┐", "", "", "", "┌" },
				{ "╮", "", "", "", "╭" }, { "<", "", "", "", "/" },
				{ "╰", "", "", " ", "" }, { "o", "", "", "", "o" },
				{ "o", "", "", "", "ツ" }, { "", "", "", "", "ﾉｼ" } };
		String afdomeme[][] = {
				{ "╹", "╹" }, // ng Unicode Character 'BOX DRAWINGS HEAVY
								// UP' (U+2579)
				{ "＞", "＜" }, { "＾", "＾" }, { "・", "・" }, { "´・", "・`" },
				{ "`・", "・´" }, { "´", "`" }, { "≧", "≦" }, { "ﾟ", "ﾟ" },
				{ "\"", "\"" }, { "･ิ", "･ิ" }, // ng(thai) HALFWIDTH
												// KATAKANA MIDDLE DOT +
												// THAI CHARACTER SARA I
				{ "❛", "❛" },// ng dingbat: HEAVY SINGLE TURNED COMMA
								// QUOTATION MARK ORNAMENT
				{ "⊙", "⊙" }, { "￣", "￣" }, // ng Halfwidth and Fullwidth
											// Forms: FULLWIDTH MACRON
				{ "◕ˇ", "ˇ◕" } // ng Geometric Shapes: 'CIRCLE WITH ALL BUT
								// UPPER LEFT QUADRANT BLACK'
		};
		String afdokuti[][] = { { "ω" }, { "∀" }, { "▽" }, { "△" }, { "Д" },
				{ "◡" }, { "A" }, { "□" }, { "～" }, { "ー" }, { "ェ" },
				{ "ρ" }, { "o" }, { "O" }, { "○" } };
		String afdhoppe[][] = { { "", "" }, { "*", "" }, { "", "*" },
				{ "", "#" }, { "#", "" }, { "✿", "" }, { "", "✿" },
				{ "", "；" }, { "；", "" }, { "｡", "｡" }, { "｡", "" },
				{ "", "｡" }, { "▰", "▰" }, // ng Geometric Shapes: BLACK
											// PARALLELOGRAM
				{ "", "▰" }, // ng
				{ "▰", "" } // ng
		};
		fdcolor = afdcolor;
		fdrinkaku = afdrinkaku;
		fdotete = afdotete;
		fdomeme = afdomeme;
		fdokuti = afdokuti;
		fdhoppe = afdhoppe;
		return;
	}

	// 候補定義: おファックなDroidSansFallback.ttfにないグリフを除去したもの
	private void defineparts_safe() {
		// 候補
		String afdcolor[][] = { { "255", "192", "203" },
				{ "128", "128", "128" }, { "136", "136", "255" } };
		String afdrinkaku[][] = { { "(", ")" }, { "(", ")" }, { "|", "|" },
				{ "[", "]" } };
		String afdotete[][] = {
				{ "", "", "", "", "" },
				{ "", "", "m", "", "" },
				{ "", "｢", "", "", "｢" }, { "", " つ", "", "", "つ" },
				{ "", " ", "", "", "o彡ﾟ" }, { "", "n", "", "", "η" },
				{ "", "∩", "", "∩", "" }, { "∩", "", "", "", "∩" },
				{ "ヽ", "", "", "", "ノ" }, { "┐", "", "", "", "┌" },
				{ "╮", "", "", "", "╭" }, { "<", "", "", "", "/" },
				{ "╰", "", "", " ", "" }, { "o", "", "", "", "o" },
				{ "o", "", "", "", "ツ" }, { "", "", "", "", "ﾉｼ" },
				{ "", "", "", "", "ゞ" },
				
		};
		String afdomeme[][] = {
				{ "＞", "＜" }, { "＾", "＾" }, { "・", "・" }, { "´・", "・`" },
				{ "`・", "・´" }, { "´", "`" }, { "≧", "≦" }, { "ﾟ", "ﾟ" },
				{ "\"", "\"" },
				{ "´", "ﾟ" },
		};
		String afdokuti[][] = { { "ω" }, { "∀" }, { "▽" }, { "△" }, { "Д" },
				{ "◡" }, { "A" }, { "□" }, { "～" }, { "ー" }, { "ェ" },
				{ "ρ" }, { "o" }, { "O" }, { "○" },{ "－" },{ "＿" },{ "∇" }, };
		String afdhoppe[][] = { { "", "" }, { "*", "" }, { "", "*" },
				{ "", "#" }, { "#", "" }, { "✿", "" }, { "", "✿" },
				{ "", "；" }, { "；", "" }, { "｡", "｡" }, { "｡", "" },
				{ "", "｡" }
		};
		fdcolor = afdcolor;
		fdrinkaku = afdrinkaku;
		fdotete = afdotete;
		fdomeme = afdomeme;
		fdokuti = afdokuti;
		fdhoppe = afdhoppe;
		return;
	}
}