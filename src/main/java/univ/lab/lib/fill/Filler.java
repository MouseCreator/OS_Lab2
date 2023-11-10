package univ.lab.lib.fill;

public interface Filler {
    <T> void fill(Object toInitialize, String attribute, T value);
}
