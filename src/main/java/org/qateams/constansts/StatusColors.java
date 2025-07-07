package org.qateams.constansts;

public class StatusColors {

    // Цвета статусов заявок (оставлены как есть)
    public static final String STATUS_APPROVED_HEX = "#088e08"; // Зеленый
    public static final String STATUS_DECLINED_HEX = "#c33117"; // Красный (без прозрачности)
    public static final String STATUS_IN_REVIEW_HEX = "#1b7eaf"; // Синий

    // RGB(A) значения для статусов (оставлены как есть)
    public static final String STATUS_APPROVED_RGB = "rgba(8, 142, 8, 1)";
    public static final String STATUS_DECLINED_RGB = "rgba(195, 49, 23, 0.95)";
    public static final String STATUS_IN_REVIEW_RGB = "rgba(27, 126, 175, 1)";

    // Цвета индикаторов шагов (ОБНОВЛЕНО)
    // Активный цвет Mui Stepper, который вы указали из CSS для .Mui-active (это зелёный).
    public static final String STEP_INDICATOR_ACTIVE_RGB = "rgb(39, 158, 23)";

    // Цвет Mui Stepper для завершенных шагов (галочка). Предполагаем, что он такой же, как активный.
    public static final String STEP_INDICATOR_COMPLETED_RGB = "rgb(39, 158, 23)";

    // Цвет Mui Stepper по умолчанию для неактивных иконок (обычно серый).
    // Это стандартный серый Mui (grey[500]). Проверьте в вашем браузере точное значение.
    public static final String STEP_INDICATOR_DEFAULT_GRAY_RGB = "rgb(117, 117, 117)"; // Общее значение для серого в Mui
}
