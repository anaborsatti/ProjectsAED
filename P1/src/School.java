import java.util.Locale;

public enum School {
    //valores possíveis para o tipo enumerado
    ENCHANTMENT("Enchantment"),
    NECROMANCY("Necromancy"),
    DIVINATION("Divination"),
    EVOCATION("Evocation"),
    ABJURATION("Abjuration"),
    TRANSMUTATION("Transmutation"),
    ILLUSION("Illusion"),
    CONJURATION("Conjuration");


    //os seguintes membros e métodos são apenas necessários porque queremos usar nomes
    //para o enumerado diferentes dos nomes internos usado pelo Java
    //Por exemplo, ENCHANTMENT é o nome usado internamente, enquanto que externamente
    //usamos Enchantment
    private String externalName;

    //Este construtor é usado apenas internamente na definição dos valores enumerados
    private School(String name)
    {
        this.externalName = name;
    }

    @Override
    public String toString()
    {
        return this.externalName;
    }

    public static School parseSchool(String s) throws SchoolParseException
    {
        for(School sc : School.values())
        {
            if(sc.externalName.equals(s)) return sc;
        }

        throw new SchoolParseException("Invalid School: " + s);
    }
}
