package ru.shift.userimporter.core.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserFilter {

    @Pattern(regexp = "^7[0-9]{10}$",
            message = "Номер должен начинается с 7 и содержать 11 символов")
    private String phone;

    @Pattern(regexp = "^[А-ЯЁ][а-яёА-ЯЁ''\\- ]{2,50}$",
            message = """
                    Имя должно начинаться с заглавной буквы
                    Содержать только кириллицу и составлять не меньше 2 символов""")
    private String name;

    @Pattern(regexp = "^[А-ЯЁ][а-яёА-ЯЁ''\\- ]{2,50}$",
            message = """
                            Имя должно начинаться с заглавной буквы и 
                            содержать только кириллицу и составлять не меньше 2 символов
                            """)
    private String  lastName;

    @Pattern(regexp = "^[A-Za-z0-9._%-]+@(shift\\.com|shift\\.ru){0,100}$",
            message = """
                    Адрес почты должен содержать только <иностранные буквы> <цифры> <.> <-> <_>
                    Обязан присутствовать символ <@>
                    Домен должен быть <shift.com> или <shift.ru>
                    """)
    private String email;

    @Max(value = 1000, message = "Максимум 1000 символов")
    private int limit = 100;

    private int offset = 0;


}