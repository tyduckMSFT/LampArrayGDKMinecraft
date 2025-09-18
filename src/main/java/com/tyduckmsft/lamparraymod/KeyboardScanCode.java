package com.tyduckmsft.lamparraymod;

public enum KeyboardScanCode
{
    SC_INVALID(0x00),
    SC_ESCAPE(0x01),

    SC_1(0x02),
    SC_2(0x03),
    SC_3(0x04),
    SC_4(0x05),
    SC_5(0x06),
    SC_6(0x07),
    SC_7(0x08),
    SC_8(0x09),
    SC_9(0x0A),
    SC_0(0x0B),
    SC_MINUS_UNDERSCORE(0x0C),
    SC_EQUALS_PLUS(0x0D),
    SC_BACKSPACE(0x0E),
    SC_TAB(0x0F),

    SC_Q(0x10),
    SC_W(0x11),
    SC_E(0x12),
    SC_R(0x13),
    SC_T(0x14),
    SC_Y(0x15),
    SC_U(0x16),
    SC_I(0x17),
    SC_O(0x18),
    SC_P(0x19),
    SC_OPENBRACKET(0x1A),
    SC_CLOSEBRACKET(0x1B),
    SC_ENTER(0x1C),
    SC_RETURN(0x1C),
    SC_LEFTCONTROL(0x1D),

    SC_A(0x1E),
    SC_S(0x1F),
    SC_D(0x20),
    SC_F(0x21),
    SC_G(0x22),
    SC_H(0x23),
    SC_J(0x24),
    SC_K(0x25),
    SC_L(0x26),
    SC_SEMICOLON_COLON(0x27),
    SC_APOSTROPHE_QUOTE(0x28),

    SC_ACCENT_TILDE(0x29),
    SC_LEFTSHIFT(0x2A),
    SC_BACKSLASH_PIPE(0x2B),

    SC_Z(0x2C),
    SC_X(0x2D),
    SC_C(0x2E),
    SC_V(0x2F),
    SC_B(0x30),
    SC_N(0x31),
    SC_M(0x32),
    SC_COMMA_LESSTHAN(0x33),
    SC_PERIOD_GREATERTHAN(0x34),
    SC_SLASH_QUESTION(0x35),
    SC_RIGHTSHIFT(0x36),

    SC_NUMPAD_MULTIPLY(0x37),

    SC_LEFTALT(0x38),
    SC_SPACE(0x39),
    SC_CAPSLOCK(0x3A),

    SC_F1(0x3B),
    SC_F2(0x3C),
    SC_F3(0x3D),
    SC_F4(0x3E),
    SC_F5(0x3F),
    SC_F6(0x40),
    SC_F7(0x41),
    SC_F8(0x42),
    SC_F9(0x43),
    SC_F10(0x44),

    SC_NUMLOCK(0x45),
    SC_SCROLLLOCK(0x46),
    SC_NUMPAD_7(0x47),
    SC_NUMPAD_8(0x48),
    SC_NUMPAD_9(0x49),
    SC_NUMPAD_SUBTRACT(0x4A),
    SC_NUMPAD_4(0x4B),
    SC_NUMPAD_5(0x4C),
    SC_NUMPAD_6(0x4D),
    SC_NUMPAD_ADD(0x4E),
    SC_NUMPAD_1(0x4F),
    SC_NUMPAD_2(0x50),
    SC_NUMPAD_3(0x51),
    SC_NUMPAD_0(0x52),
    SC_NUMPAD_DECIMAL(0x53),

    SC_F11(0x57),
    SC_F12(0x58),

    SC_NUMPAD_EQUALS(0x59),

    SC_KEYBOARDLANG4(0x77),
    SC_KEYBOARDLANG3(0x78),
    SC_KEYBOARDINTL4(0x79),
    SC_KEYBOARDINTL5(0x7B),
    SC_KEYBOARDINTL2(0x7D),
    SC_NUMPADCOMMA(0x7E),
    SC_KEYBOARDLANG2(0xF1),
    SC_KEYBOARDLANG1(0xF2),

    SC_SCANPREVTRACK(0xE010),
    SC_SCANNEXTTRACK(0xE019),

    SC_NUMPAD_ENTER(0xE01C),
    SC_RIGHTCONTROL(0xE01D),

    SC_MUTE(0xE020),
    SC_CALCULATOR(0xE021),
    SC_PLAYPAUSE(0xE022),
    SC_STOP(0xE024),
    SC_VOLUMEDOWN(0xE02E),
    SC_VOLUMEUP(0xE030),

    SC_NUMPAD_DIVIDE(0xE035),
    SC_PRINTSCREEN(0xE037),
    SC_RIGHTALT(0xE038),

    SC_BREAK(0xE046),
    SC_BREAK2(0xE0C6),

    SC_HOME(0xE047),
    SC_UPARROW(0xE048),
    SC_PAGEUP(0xE049),
    SC_LEFTARROW(0xE04B),
    SC_RIGHTARROW(0xE04D),
    SC_END(0xE04F),
    SC_DOWNARROW(0xE050),
    SC_PAGEDOWN(0xE051),
    SC_INSERT(0xE052),
    SC_DELETE(0xE053),

    SC_LEFTGUI(0xE05B),
    SC_RIGHTGUI(0xE05C),
    SC_APP(0xE05D),
    SC_KEYBOARDPOWER(0xE05E),
    SC_SYSTEMPOWER(0xE05E),

    SC_PAUSE(0xE11D45),
    SC_PAUSE2(0xE19DC5);

    private final int code;

    KeyboardScanCode(int code)
    {
        this.code = code;
    }

    public int getCode()
    {
        return code;
    }
};

