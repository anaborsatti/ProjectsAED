import java.io.BufferedReader;

public class Spell {

    private String name; //cant be null
    private int lvl; //between 0-9
    private School school; //
    private String castingTime;
    private int range; //>0 1range = 6´´
    private boolean concentration;
    private int duration;
    private String description;

    public Spell(String name, int lvl, School school, String castingTime, int range, int duration, boolean concentration, String description)
    {
        if (name == null)
            throw new IllegalArgumentException("Name cannot be null");
        if (lvl > 9 || lvl < 0)
            throw new IllegalArgumentException("Level must be between 0 and 9");
        if (range < 0)
            throw new IllegalArgumentException("Range cannot be negative");
        if (duration < 0)
            throw new IllegalArgumentException("Duration cannot be negative");
        if (description == null)
            throw new IllegalArgumentException("Description cannot be null");
        if (school == null)
            throw new IllegalArgumentException("School cannot be null");
        if (castingTime == null)
            throw new IllegalArgumentException("CastingTime cannot be null");

        this.name = name;
        this.lvl = lvl;
        this.school = school;
        this.castingTime = castingTime;
        this.range = range;
        this.concentration = concentration;
        this.description = description;
        this.duration = duration;

    }

    public String getName()
    {
        return this.name;
    }

    public String getCastingTime()
    {
        return this.castingTime;
    }

    public School getSchool()
    {
        return this.school;
    }

    public int getLvl()
    {
        return this.lvl;
    }

    public int getRange()
    {
        return this.range;
    }

    public int getDuration()
    {
        return this.duration;
    }

    public boolean getConcentration()
    {
        return this.concentration;
    }

    public String getDescription()
    {
        return this.description;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setCastingTime(String castingTime)
    {
        this.castingTime = castingTime;
    }

    public void setSchool(School school)
    {
        this.school = school;
    }

    public void setLvl(int lvl)
    {
        this.lvl = lvl;
    }

    public void setRange(int range)
    {
        this.range = range;
    }

    public void setDuration(int duration)
    {
        this.duration = duration;
    }

    public void setConcentration(boolean concentration)
    {
        this.concentration = concentration;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public boolean equals(Spell s2)
    {
        if (s2 == null)
            return false;
        return this.name.equals(s2.name);
    }

    @Override
    public boolean equals(Object o)
    {
        if(o == null) return false;
        if(!(o instanceof Spell)) return false;
        return equals((Spell) o);
    }

    public int compareToA(Spell s2)
    {
        if (this.lvl != s2.lvl)
            return Integer.compare(this.lvl, s2.lvl);
        else
            return this.name.compareTo(s2.name);
    }

    public int compareTo(Spell s2)
    {
        int result = Integer.compare(this.lvl, s2.lvl);
        if (result == 0)
            result = this.name.compareTo(s2.name);
        return result;
    }


    private String durationAsString (int duration){
        String result;
        if (duration == 0)
            result = "Instantaneous";
        else if (duration < 11)
            result = duration + " rounds";
        else if (duration <= 599)
            result = duration/10 + " minutes";
        else
            result = duration/600 + " hours";
        return result;
    }

    private String concentrationAsString (boolean concentration){
        String result;
        return concentration ? " (Concentration)" : "";
    }

    public String toString()
    {
        String result = "------------------------------\n" ;
        result += this.name + "\n" ;
        result += "Level: " + this.lvl + ", " + this.school + "\n" ;
        result += "Casting Time: " + this.castingTime + "\n" ;
        result += "Range: " + this.range + " feet" + "\n" ;
        result += "Duration: " + durationAsString(this.duration) + concentrationAsString(this.concentration) + "\n" ;
        result += this.description + "\n";
        result += "------------------------------";

        return result;

    }

    private int concentrationAsNumber (boolean concentration){
        return concentration ? 1 : 0;
    }


        public String toSaveString()
    {
        String result = this.name + "\n";
        result += this.lvl + "\n";
        result += this.school + "\n";
        result += this.castingTime + "\n";
        result += this.range + "\n";
        result += this.duration+ "\n";
        result += concentrationAsNumber(this.concentration) + "\n";
        result += this.description + "\n\\EOD";
        return result;
    }

    public Spell copy()
    {
        Spell result = new Spell(this.name, this.lvl, this.school, this.castingTime, this.range, this.duration, this.concentration, this.description);
        return result;
    }


    public static void unitTestToString () {
        {
            Spell s = new Spell("aaa", 3, School.ENCHANTMENT, "bbb", 1000, 600, true, "blablabla");
            assert s.toString().equals("""
                    ------------------------------
                    aaa
                    Level: 3, ENCHANTMENT
                    Casting Time: bbb
                    Range: 1000 feet
                    Duration: 1 hours(Concentration)
                    blablabla
                    ------------------------------
                    """) : s.toString();
        }
        {
            Spell s = new Spell("zzz", 3, School.ABJURATION, "bbb", 1000, 50, false, "blablabla");
            assert s.toString().equals("""
                    ------------------------------
                    zzz
                    Level: 3, ABJURATION
                    Casting Time: bbb
                    Range: 1000 feet
                    Duration: 5 minutes
                    blablabla
                    ------------------------------
                    """) : s.toString();
        }

    }

    public static void unitTestSaveString (){
        Spell s = new Spell ("ana", 4, School.ENCHANTMENT, "clara", 2000, 100, true, "test");
        assert s.toSaveString().equals("""
                ana
                4
                ENCHANTMENT
                clara
                2000
                10 minutes
                1
                test
                """) : s.toSaveString();
    }

    public static void unitTestEquals (){
        Spell s1 = new Spell("ana", 4, School.ENCHANTMENT, "clara", 2000, 100, true, "test");
        Spell s2 = s1.copy();
        Spell s3 = new Spell("zzz", 3, School.ABJURATION, "bbb", 1000, 50, false, "blablabla");
        assert s1.equals(s2) : s1.equals(s2);
        assert !s2.equals(s3) : s2.equals(s3);
    }

    public static void unitTestCompareTo (){
        Spell s1 = new Spell("ana", 4, School.ENCHANTMENT, "clara", 2000, 100, true, "test");
        Spell s2 = new Spell ("barbara", 4, School.ENCHANTMENT, "clara", 2000, 100, true, "test");
        Spell s3 = new Spell("francisco", 6, School.ENCHANTMENT, "clara", 2000, 100, true, "test");
        assert s1.compareTo(s2) == -1 : s1.equals(s2);
        assert s1.compareTo(s3) == -1 : s1.equals(s3);
    }

    public static void unitTests (){
        unitTestToString();
        unitTestSaveString();
        unitTestEquals();
        unitTestCompareTo();
    }


}
