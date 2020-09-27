package fr.iconvoit.entity

import java.io.IOException;

public class ParserIcs {

    private  final Slot sl;
    
    public void extraction_file(String name[]){
        File dataFile = new File("test.txt");
        String REGEX,REGEX2;
        REGEX = "[A-HJ-NP-Za-hj-np-z]";
        if(!file.exist()){
            try{
            file.createNewFile();
            }catch(IOException e){
                e.printStackTrace();
            }
        }

        try {
            InputStream ips = new FileInputStream(dataFile);
            InputStreamReader ipsr = new InputStreamReader(ips);
            BufferedReader br = new BufferedReader(ipsr);
            String ligne;

            while ((ligne = br.readLine()) != null) {
            // recuperation de la ligne courante
            System.out.println("Contenu de la ligne:" + ligne);
            // separation de la ligne avec le toke " " (espace)
            //

            String token = " ";
            StringTokenizer stringTokenizer = new StringTokenizer(ligne, token);

            // Parcours des tokens de la ligne
            while (stringTokenizer.hasMoreElements()) {
            String element = (String) stringTokenizer.nextElement();
            System.out.println("Element : " + element);
        
        }
    }
    br.close();
    } catch (Exception e) {
    System.out.println(e.toString());
    }

    }
}
