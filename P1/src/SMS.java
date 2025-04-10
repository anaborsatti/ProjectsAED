import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.function.Consumer;


public class SMS {


    private SpellBook book;
    private SpellBook currentView;
    private final HashMap<String, Consumer<String>> cmdFunctions;
    //buffered reader that can be used to read from stdio
    //it will be initialized in the run method
    private BufferedReader reader;

    public SMS()
    {
        this.book = new SpellBook();
        this.currentView = this.book.shallowCopy();

        this.cmdFunctions = new HashMap<>();
        this.cmdFunctions.put("LOAD", this::cmdLoad);
        this.cmdFunctions.put("SAVE",this::cmdSave);
        this.cmdFunctions.put("DELETE",this::cmdDelete);
        this.cmdFunctions.put("PRINT",this::cmdPrint);
        this.cmdFunctions.put("ADD",this::cmdAdd);
        this.cmdFunctions.put("UPDATE",this::cmdUpdate);
        this.cmdFunctions.put("COPY",this::cmdCopy);
        this.cmdFunctions.put("FILTER",this::cmdFilter);
        this.cmdFunctions.put("RESET",this::cmdReset);
        this.cmdFunctions.put("LIST",this::cmdList);
        this.cmdFunctions.put("SAVEVIEW",this::cmdSaveView);
        this.cmdFunctions.put("ORDER",this::cmdOrder);
    }

    private void cmdLoad(String arguments)
    {

        if(arguments.isEmpty())
        {
            System.out.println(MSG.INVALID_CMD_ARGS);
            return;
        }

        String[] splitResult = arguments.split(" ");
        SpellBook newBook = SpellBook.readSpellsFromFile(splitResult[0]);
        if(newBook != null)
        {
            this.book = newBook;
            this.currentView = this.book.shallowCopy();
            System.out.println(this.book.countSpells() + MSG.LOADED);
        }
    }

    private void cmdSave(String arguments)
    {
        if(arguments.isEmpty())
        {
            System.out.println(MSG.INVALID_CMD_ARGS);
            return;
        }
        String[] splitResult = arguments.split(" ");
        if(this.book.saveSpellsToFile(splitResult[0]))
        {
            System.out.println(this.book.countSpells() + MSG.SAVED);
        }
    }

    private void cmdDelete(String arguments)
    {
        if(arguments.isEmpty())
        {
            System.out.println(MSG.INVALID_CMD_ARGS);
            return;
        }
        String[] splitResult = arguments.split("\"");
        if(splitResult.length < 2)
        {
            System.out.println(MSG.INVALID_CMD_ARGS);
            return;
        }
        if(this.book.deleteSpell(splitResult[1]))
        {
            System.out.println(MSG.DELETED);
            this.currentView = this.book.shallowCopy();
        }
        else {
            System.out.println(MSG.NO_SUCH_SPELL);
        }
    }

    private void cmdPrint(String arguments)
    {
        if(arguments.isEmpty())
        {
            System.out.println(MSG.INVALID_CMD_ARGS);
            return;
        }
        String[] splitResult = arguments.split("\"");

        if(splitResult.length < 2)
        {
            System.out.println(MSG.INVALID_CMD_ARGS);
            return;
        }

        Spell s = this.book.getSpell(splitResult[1]);
        if(s== null)
        {
            System.out.println(MSG.NO_SUCH_SPELL);
        }
        else {
            System.out.println(s);
        }
    }

    private void cmdAdd(String arguments)
    {
        if(this.reader == null) return;

        Spell s = SpellBook.readSpell(this.reader);
        if(s != null)
        {
            this.book.addSpell(s);
            this.currentView = book.shallowCopy();
            System.out.println(MSG.ADDED);
        }
    }


    private void cmdCopy(String arguments)
    {
        String[] args = arguments.split("\"");
        //System.out.println(Arrays.toString(args));
        if (args.length != 4) //" ", spell, space, spell 2,
        {
            System.out.println(MSG.INVALID_CMD_ARGS);
            return;
        }
        if (book.getSpell(args[1]) == null)
        {
            System.out.println(MSG.NO_SUCH_SPELL);
            return;
        }
        if (book.getSpell(args[3]) != null)
        {
            System.out.println(MSG.DUPLICATE + args[3]);
            return;
        }
        Spell s1 = book.getSpell(args[1]);
        Spell s2 = s1.copy();
        s2.setName(args[3]);
        book.addSpell(s2);
        this.currentView = this.book.shallowCopy();
        System.out.println(MSG.ADDED);
    }



    private void cmdFilter(String arguments)
    {
        String[] args = arguments.split(" ");
        if (args.length != 3)
        {
            System.out.println(MSG.INVALID_CMD_ARGS);
            return;
        }
        if (!(args[0].equals("lvl") || args[0].equals("ran") || args[0].equals("dur")))
        {
            System.out.println(MSG.INVALID_CMD_ARGS);
            return;
        }
        int a;
        int b;
        try {
            a = Integer.parseInt(args[1]);
            b = Integer.parseInt(args[2]);
        }
        catch (NumberFormatException e) {
            System.out.println(MSG.INVALID_CMD_ARGS);
            return;
        }
        switch  (args[0]){
            case "lvl" -> this.currentView = this.currentView.filterSpells(x -> x.getLvl() >= a && x.getLvl() <= b) ;
            case "ran" -> this.currentView = this.currentView.filterSpells(x -> x.getRange() >= a && x.getRange() <= b);
            case "dur" -> this.currentView = this.currentView.filterSpells(x -> x.getDuration() >= a && x.getDuration() <= b);
        }
        System.out.println(MSG.VIEW_FILTERED + this.currentView.countSpells() + MSG.SPELLS_SELECTED);
    }

    private void cmdOrder1 ()
    {
        this.currentView.sort(Spell::compareTo);
        System.out.println(currentView.countSpells() + MSG.VIEW_ORDERED);
    }

    private void cmdOrder3 (String s1, String s2)
    {
        if (s1.equals("nam") && (s2.equals("ASC") || s2.equals("DES"))){
            switch (s2) {
                case "ASC" -> this.currentView.sort(Comparator.comparing(Spell::getName));
                case "DES" -> this.currentView.sort(Comparator.comparing(Spell::getName).reversed());
            }
        }
        else if (s1.equals("lvl") && (s2.equals("ASC") || s2.equals("DES"))){
            switch (s2) {
                case "ASC" -> this.currentView.sort(Comparator.comparing(Spell::getLvl));
                case "DES" -> this.currentView.sort(Comparator.comparing(Spell::getLvl).reversed());
            }
        }
        else{
            System.out.println(MSG.INVALID_CMD_ARGS);
            return;
        }
        System.out.println(currentView.countSpells() + MSG.VIEW_ORDERED);
    }

    private void cmdOrder(String arguments)
    {
        String[] args = arguments.split(" ");
        if (args.length == 1 && args[0].isEmpty())
            cmdOrder1();
        else if (args.length == 2)
            cmdOrder3(args[0], args[1]);
        else
            System.out.println(MSG.INVALID_CMD_ARGS);
    }

    private static String readDescriptionSpecial (BufferedReader br) throws IOException {
        ArrayList<String> lines = new ArrayList<>();
        String line;
        line = br.readLine();
        if (line == null)
            throw new IOException ();
        lines.add(line);
        if (line.equals("*"))
            return line;
        while ((line = br.readLine()) != null) {
            if (line.equals("\\EOD"))
                break;
            lines.add(line);
        }
        return String.join("\n", lines);
    }

    private void cmdUpdate(String arguments)  {
        String[] args = arguments.split(" ");
        String name = "";
        String lvlS = "";
        String schoolS = "";
        String castingTime = "";
        String rangeS = "";
        String durationS = "";
        String concentrationS = "";
        String description = "";
        try {
            name = this.reader.readLine();
            lvlS = this.reader.readLine();
            schoolS = this.reader.readLine();
            castingTime = this.reader.readLine();
            rangeS = this.reader.readLine();
            durationS = this.reader.readLine();
            concentrationS = this.reader.readLine();
            description = readDescriptionSpecial (this.reader);
            //System.out.println(name + lvlS + schoolS + castingTime + rangeS + durationS + concentrationS);
            //System.out.println(description);
        }
        catch (Exception e){
            System.out.println(MSG.INVALID_CMD_ARGS);
        }
        if (book.getSpell(name) != null){
            if (!name.equals("*"))
                book.getSpell(name).setName(name);
            if (!lvlS.equals("*"))
                book.getSpell(name).setLvl(Integer.parseInt(lvlS));
            if (!schoolS.equals("*"))
                book.getSpell(name).setSchool(School.parseSchool(schoolS));
            if (!castingTime.equals("*"))
                book.getSpell(name).setCastingTime(castingTime);
            if (!rangeS.equals("*"))
                book.getSpell(name).setRange(Integer.parseInt(rangeS));
            if (!durationS.equals("*"))
                book.getSpell(name).setDuration(Integer.parseInt(durationS));
            if (!concentrationS.equals("*"))
                book.getSpell(name).setConcentration(Integer.parseInt(concentrationS) != 0);
            if (!description.equals("*"))
                book.getSpell(name).setDescription(description);
            System.out.println(MSG.SPELL_UPDATED + name);
        }
        else
            System.out.println(MSG.NO_SUCH_SPELL);
    }

    private void cmdReset(String arguments)
    {
        this.currentView = this.book.shallowCopy();
        System.out.println(MSG.VIEW_RESET + this.currentView.countSpells() + MSG.SPELLS_SELECTED);
    }

    private void cmdList(String arguments)
    {
        int n = this.currentView.countSpells();
        if(!arguments.isEmpty())
        {
            try
            {
                n = Integer.parseInt(arguments);
            }
            catch(Exception e)
            {
                System.out.println(MSG.INVALID_CMD_ARGS);
                return;
            }
        }

        this.currentView.printSpells(n);
    }

    private void cmdSaveView(String arguments)
    {
        if(arguments.isEmpty())
        {
            System.out.println(MSG.INVALID_CMD_ARGS);
            return;
        }

        String[] splitResult = arguments.split(" ");
        if(this.currentView.saveSpellsToFile(splitResult[0]))
        {
            System.out.println(this.currentView.countSpells() + MSG.SAVED);
        }
    }



    public void run()
    {
        try {
            this.reader = new BufferedReader(new InputStreamReader(System.in));
            String line;
            String[] arguments;
            Consumer<String> cmd;

            System.out.print(MSG.PROMPT);
            while ((line = this.reader.readLine()) != null) {
                arguments = line.split(" ",2);
                cmd = this.cmdFunctions.get(arguments[0]);
                if(cmd!=null)
                {
                    if(arguments.length == 1)
                    {
                        cmd.accept("");
                    }
                    else {
                        cmd.accept(arguments[1]);
                    }
                }
                else
                {
                    System.out.println(MSG.UNKNOWN_CMD);
                }
                System.out.print(MSG.PROMPT);
            }

            this.reader.close();
        }
        catch (IOException io)
        {
            io.printStackTrace();
        }
    }

}
