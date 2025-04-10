import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.Predicate;
import java.util.List;
import java.util.Scanner;

public class SpellBook {
    private ArrayList<Spell> spells;

    public SpellBook()
    {
        this.spells = new ArrayList<>();
    }

    public boolean addSpell(Spell spell)
    {
        if (getSpell(spell.getName()) == null) {
            this.spells.add(spell);
            return true;
        }
        return false;
    }

    public int countSpells()
    {
        return this.spells.size();
    }

    public Spell getSpell(String spellName)
    {
        for (Spell spell : this.spells)
            if (spell.getName().equals(spellName))
                return spell;

        return null;
    }

    public boolean deleteSpell(String spellName)
    {
        return this.spells.removeIf(spell -> spell.getName().equals(spellName)); //predicado
    }

    public void printSpells(int n)
    {
        for (int i = 0; i < Math.min(n, countSpells()); i++)
            System.out.println(this.spells.get(i).toString());
    }

    public SpellBook filterSpells(Predicate<Spell> filter)
    {
        SpellBook result = new SpellBook();
        for (Spell spell : this.spells)
            if (filter.test(spell))
                result.addSpell(spell);
        return result;
    }

    @SuppressWarnings("unchecked")
    public SpellBook shallowCopy()
    {
        SpellBook result = new SpellBook();
        for (Spell spell : this.spells)
            result.addSpell(spell); //copy pointers
        return result;
    }

    public void sort(Comparator<Spell> c)
    {
        spells.sort(c);
    }

    public boolean saveSpellsToFile(String fileName)
    {
        try {
            File file = new File(fileName);
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            for (Spell spell : spells)
            {
                bw.write(spell.toSaveString() + "\n"); //colocar uma linha a mais entre cada feitiço
            }

            bw.flush();
            bw.close();
            return true;
        } catch (IOException ioe) {
            return false;
        }
    }

    private static String readDescription(BufferedReader br) {
        StringBuilder result = new StringBuilder();
        String line;
        try {
            while (!(line = br.readLine()).equals("\\EOD")) {
                result.append(line).append("\n");
            }
            if (result.length() > 0 && result.charAt(result.length() - 1) == '\n') { //nao consegui tirar o \n e estava com uma linha a mais
                result.setLength(result.length() - 1);
            }
        } catch (IOException ignore) {
        }
        return result.toString();
    }

    public static Spell readSpell(BufferedReader br)
    {
        Spell result = null;
        String name = ""; //ler tudo e depois converter
        int lvl;
        School school;
        String castingTime;
        int range;
        int duration;
        boolean concentration;
        String description;

        try {
            name = br.readLine();
            if (name != null) {
                String lvlS = br.readLine();
                String schoolS = br.readLine();
                castingTime = br.readLine();
                String rangeS = br.readLine();
                String durationS = br.readLine();
                String concentrationS = br.readLine();
                description = readDescription(br);

                lvl = Integer.parseInt(lvlS);
                school = School.parseSchool(schoolS);
                range = Integer.parseInt(rangeS);
                duration = Integer.parseInt(durationS);
                concentration = Integer.parseInt(concentrationS) != 0;


                try {
                    result = new Spell(name, lvl, school, castingTime, range, duration, concentration, description);
                }
                catch (Exception e) {
                    System.out.println(MSG.INVALID_SPELL + name);
                    return null;
                }
            }
        }

        catch (Exception exception)
        {
            System.out.println(MSG.INVALID_SPELL + name);
            return null;
        }
        return result;
    }


    public static SpellBook readSpellsFromFileA(String fileName)
    {
        SpellBook result = new SpellBook();
        try
        {
            File file = new File(fileName);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            Spell spell;
            while ((spell = readSpell(br)) != null) {
                result.addSpell(spell);
            }
            fr.close();
            return result;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.out.println(MSG.INVALID_FILE);
            return null;
        }

    }

    public static SpellBook readSpellsFromFile(String fileName)
    {
        SpellBook result = new SpellBook();
        try
        {
            File file = new File(fileName);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            Spell spell;
            while (br.ready()) {
                spell = readSpell(br);
                if (spell != null)
                    result.addSpell(spell);
            }
            fr.close();
            return result;
        }
        catch(Exception e)
        {
            System.out.println(MSG.INVALID_FILE);
            return null;
        }

    }

    private static void testReadSpells (String[] args){
        SpellBook book = readSpellsFromFile(args[0]);
        assert book != null;
        book.printSpells(book.countSpells());

    }


    public static void main (String[] args){
        testReadSpells(args);
    }
}
