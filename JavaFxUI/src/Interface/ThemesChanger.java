package Interface;

public interface ThemesChanger {
    enum Themes { Default, Theme_1 {
        @Override
        public String toString() {
            return super.toString().replace("_", " ");
        }
    }, Theme_2 {
        @Override
        public String toString() {
            return super.toString().replace("_", " ");
        }
    }}

    void setNewTheme(Themes theme);
}