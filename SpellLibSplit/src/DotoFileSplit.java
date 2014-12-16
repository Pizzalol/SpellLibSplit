/*******************************************************************************
* Copyright (c) Atteroeu@gmail.com
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>.
******************************************************************************/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class DotoFileSplit {
        public static ArrayList<String> currentAbility = new ArrayList<String>();
        public static File dir = new File("dotoOutput");

        public static void main(String[] args) {
                if (!dir.isDirectory()) {
                        dir.mkdir();
                }
                BufferedReader br = null;
                try {
                        br = new BufferedReader(new FileReader("npc_abilities.txt"));
                        String input = br.readLine();
                        while (!(input = br.readLine()).startsWith("\t//==")) {}
                        while (input != null) {
                                if (input.startsWith("\t//==")) {
                                        if (!currentAbility.isEmpty())
                                                saveAbility();
                                        input = br.readLine();
                                        while (input != null && !input.startsWith("\t//==")) {
                                                input = br.readLine();
                                        }
                                        input = br.readLine();
                                }
                                currentAbility.add(input);
                                input = br.readLine();
                        }
                } catch (IOException e) {
                        System.out.println("put npc_abilities.txt in the folder from where you run this");
                        e.printStackTrace();
                } finally {
                        try {
                                if (br != null)
                                        br.close();
                        } catch (IOException ex) {
                                ex.printStackTrace();
                        }
                }
        }

        private static void saveAbility() {
                try {
                        String abilityPureName = currentAbility.get(0).substring(2, currentAbility.get(0).length()-1);
                        String abilityName = abilityPureName + "_datadriven";
                        String content = "";

                        currentAbility.remove(0);
                        content+="\"" + abilityName + "\"" + System.lineSeparator();
                        for (String s : currentAbility) {
                                if (s.contains("\"ID\"")) {
                                        content+= "\t\"BaseClass\"\t\t\t\t" + "\"ability_datadriven\"" + System.lineSeparator();
                                } else if (s.contains("FightRecapLevel")) {
                                        content+= (s.length() == 0 ? "" : s.substring(1)) + System.lineSeparator();

                                        content+= "\t\"AbilityTextureName\"\t\t\t\"" + abilityPureName + "\"" + System.lineSeparator();
                                } else {
                                        content+= (s.length() == 0 ? "" : s.substring(1)) + System.lineSeparator();
                                }
                        }

                        File ability = new File(dir, abilityName);
                        FileOutputStream outputStream = new FileOutputStream(ability +".txt");
                        outputStream.write(content.getBytes());
                        outputStream.close();
                        System.out.println(abilityName);
                } catch (Exception e) {
                        e.printStackTrace();
                }
                currentAbility.clear();
        }
}
