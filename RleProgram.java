//ArrayList and Scanner imported due to their use in this program
import java.util.ArrayList;
import java.util.Scanner;

public class RleProgram {
    public static String hexToRle(String hex){
        //creates array with every substring that is between a colon (:)
        String[] hexArray = hex.split(":");
        String newHex = "";
        for (int i = 0; i < hexArray.length; i++) {
            String b = hexArray[i];
            if (hexArray[i].length() == 2){
                hexArray[i] = hexArray[i];
            } else {
                //adds hexadecimal of 10 to 15 along with the third character of original value
                if (hexArray[i].charAt(0) == '1' && hexArray[i].charAt(1) == '0'){
                    hexArray[i] = ("A" + b.charAt(2));
                } else if (hexArray[i].charAt(0) == '1' && hexArray[i].charAt(1) == '1'){
                    hexArray[i] = ("B" + b.charAt(2));
                } else if (hexArray[i].charAt(0) == '1' && hexArray[i].charAt(1) == '2'){
                    hexArray[i] = ("C" + b.charAt(2));
                } else if (hexArray[i].charAt(0) == '1' && hexArray[i].charAt(1) == '3'){
                    hexArray[i] = ("D" + b.charAt(2));
                } else if (hexArray[i].charAt(0) == '1' && hexArray[i].charAt(1) == '4'){
                    hexArray[i] = ("E" + b.charAt(2));
                } else if (hexArray[i].charAt(0) == '1' && hexArray[i].charAt(1) == '5'){
                    hexArray[i] = ("F" + b.charAt(2));
                }
            }
        }
        //concatenates values of array into a string
        for (int j = 0; j < hexArray.length; j++){
            newHex += hexArray[j];
        }
        return newHex;
    }
    public static String toHexString(byte[] data){
        String dataAsHexString = "";
        for (int i = 0; i < data.length; i++) {
            String bitToHex = "";
            //concatenates string with equivalent hexadecimal values to original decimal (byte) values
            if (data[i] < 10) {
                bitToHex += data[i];
            } else if (data[i] == 10) {
                bitToHex += "a";
            } else if (data[i] == 11) {
                bitToHex += "b";
            } else if (data[i] == 12) {
                bitToHex += "c";
            } else if (data[i] == 13) {
                bitToHex += "d";
            } else if (data[i] == 14) {
                bitToHex += "e";
            } else if (data[i] == 15) {
                bitToHex += "f";
            }
            dataAsHexString += bitToHex;
        }
        return dataAsHexString;
    }
    public static int countRuns(byte[] flatData){
        byte previousByte = flatData[0], currentByte;
        int numRuns = 1, numSame = 0;
        for (int i = 0; i < flatData.length; i++) {
            currentByte = flatData[i];
            //checks to see if the previous byte is the same as the current byte, then increments numRuns
            if (previousByte != currentByte){
                numRuns++;
            } else {
                numSame++;
            }
            if (numSame > 15){
                //adds another run if the total number of the same byte exceeds 15
                numRuns++;
                numSame = 0;
            }
            previousByte = flatData[i];
        }
        //returns the total number of runs in the initial byte array
        return numRuns;
    }
    public static byte[] encodeRle(byte[] flatData){
        byte numByte = 0, actualByte = flatData[0];
        int numIndexes = 0;
        //allows for an array to continually accept new values (no set array length)
        ArrayList<Byte> rleData = new ArrayList<Byte>();
        for (int i = 0; i < flatData.length; i++) {
            //essentially performs the same things as countRuns
            if (flatData[i] == actualByte){
                numByte++;
            } else {
                //adds 2 to the number of indexes of the (eventual) final byte array
                numIndexes += 2;
                rleData.add(numByte);
                rleData.add(actualByte);
                numByte = 1;
                actualByte = flatData[i];
            }
            //adds "new" run of the same byte if the total number of the byte exceeds 15
            if (numByte > 15){
                byte fifteen = 15;
                numIndexes += 2;
                rleData.add(fifteen);
                rleData.add(actualByte);
                numByte -= 15;
            }
            //adds the amount of the final byte and the final byte itself
            if (i == flatData.length - 1){
                rleData.add(numByte);
                rleData.add(actualByte);
            }
        }
        //declares the final array and adds the corresponding bytes from the ArrayList
        byte[] rleArray = new byte[numIndexes + 2];
        for (int j = 0; j < rleData.size(); j++){
            rleArray[j] = rleData.get(j);
        }
        return rleArray;
    }
    public static int getDecodedLength(byte[] rleData){
        //finds the total size of a byte array via adding the individual byte values contained within it
        int decompressedSize = 0;
        for (int i = 0; i < rleData.length; i += 2) {
            decompressedSize += rleData[i];
        }
        return decompressedSize;
    }
    public static byte[] decodeRle(byte[] rleData){
        byte currentByte = rleData[0];
        ArrayList<Byte> decodedData = new ArrayList<Byte>();
        for (int i = 0; i < rleData.length; i++){
            //if the current index is even, set the current byte to the corresponding value in rleData
            if (i % 2 == 0){
                currentByte = rleData[i];
            } else {
                //if the current index is odd, add the number of the byte in the current index in rleData, currentByte times
                for (byte j = 0; j < currentByte; j++){
                    decodedData.add(rleData[i]);
                }
            }
        }
        byte[] decodedRle = new byte[decodedData.size()];
        //declares the final array and adds the corresponding bytes from the ArrayList
        for (int k = 0; k < decodedData.size(); k++){
            decodedRle[k] = decodedData.get(k);
        }
        return decodedRle;
    }
    public static byte[] stringToData(String dataString){
        //ensures that the characters read will always be lowercase if they are letters
        dataString = dataString.toLowerCase();
        byte[] stringData = new byte[dataString.length()];
        for (int i = 0; i < dataString.length(); i++) {
            //sets corresponding byte values to each index given a certain hexadecimal value
            if (dataString.charAt(i) == '0'){
                stringData[i] = 0;
            } else if (dataString.charAt(i) == '1'){
                stringData[i] = 1;
            } else if (dataString.charAt(i) == '2'){
                stringData[i] = 2;
            } else if (dataString.charAt(i) == '3'){
                stringData[i] = 3;
            } else if (dataString.charAt(i) == '4'){
                stringData[i] = 4;
            } else if (dataString.charAt(i) == '5'){
                stringData[i] = 5;
            } else if (dataString.charAt(i) == '6'){
                stringData[i] = 6;
            } else if (dataString.charAt(i) == '7'){
                stringData[i] = 7;
            } else if (dataString.charAt(i) == '8'){
                stringData[i] = 8;
            } else if (dataString.charAt(i) == '9'){
                stringData[i] = 9;
            } else if (dataString.charAt(i) == 'a'){
                stringData[i] = 10;
            } else if (dataString.charAt(i) == 'b'){
                stringData[i] = 11;
            } else if (dataString.charAt(i) == 'c'){
                stringData[i] = 12;
            } else if (dataString.charAt(i) == 'd'){
                stringData[i] = 13;
            } else if (dataString.charAt(i) == 'e'){
                stringData[i] = 14;
            } else if (dataString.charAt(i) == 'f'){
                stringData[i] = 15;
            }
        }
        //returns the final edited byte array
        return stringData;
    }
    public static String toRleString(byte[] rleData){
        String rleString = "";
        String[] originalRleData = new String[rleData.length];
        for (int h = 0; h < rleData.length; h++) {
            //stores values from rleData as strings in corresponding indexes
            originalRleData[h] = ("" + rleData[h]);
        }
        for (int i = 0; i < rleData.length; i++) {
            //if the index is even, concatenate rleString with the same byte value
            if (i % 2 == 0){
                rleString += rleData[i];
            } else {
                //if the index is odd, concatenate rleString with the same byte value in hexadecimal
                    if (rleData[i] < 10) {
                        rleString += rleData[i];
                    } else if (rleData[i] == 10) {
                        rleString += "a";
                        originalRleData[i] = "a";
                    } else if (rleData[i] == 11) {
                        rleString += "b";
                        originalRleData[i] = "b";
                    } else if (rleData[i] == 12) {
                        rleString += "c";
                        originalRleData[i] = "c";
                    } else if (rleData[i] == 13) {
                        rleString += "d";
                        originalRleData[i] = "d";
                    } else if (rleData[i] == 14) {
                        rleString += "e";
                        originalRleData[i] = "e";
                    } else if (rleData[i] == 15) {
                        rleString += "f";
                        originalRleData[i] = "f";
                    }
                }
            }
        String finalString = "";
        int colonNumber = 0;
        for (int j = 0; j < rleString.length(); j = j + 2){
            //done so that the next if statement will not overshoot the length of the array
            if ((j - 1) > rleString.length()){
                break;
            }
            if ((j - 1) % 2 == 1){
                //concatenates finalString with the original array data
                for (int k = 0; k <= originalRleData.length; k = k + 2){
                    if ((k + 1) >= originalRleData.length){
                        break;
                    }
                    finalString += originalRleData[k] + originalRleData[k + 1] + ":";
                }
            }
            colonNumber = j;
        }
        String placeholder = "";
        //determines the length of finalString, if odd then has one more character than if even
        if ((rleString.length() + colonNumber / 2 - rleString.length() % colonNumber) % 2 == 1){
            placeholder = finalString.substring(0, (rleString.length() + colonNumber / 2 - rleString.length() % colonNumber + 2));
        } else {
            placeholder = finalString.substring(0, (rleString.length() + colonNumber / 2 - rleString.length() % colonNumber + 1));
        }
            finalString = placeholder;
        //removes a colon from the end of finalString if it ends with one
        if (finalString.endsWith(":")){
            finalString = finalString.substring(0, finalString.length() - 1);
        }
        return finalString;
    }
    public static byte[] stringToRle(String rleString){
        //stores equivalent hex values of rleString in newRleString, using hexToRle()
        String newRleString = hexToRle(rleString);
        byte[] rleData = new byte[newRleString.length()];
        //ensures that the characters read will all be lowercase
        newRleString = newRleString.toLowerCase();
        for (int i = 0; i < newRleString.length(); i++) {
            //stores equivalent decimal values in rleData (byte array) to the hexadecimal values stored in newRleString
            if (newRleString.charAt(i) == '0'){
                rleData[i] = 0;
            } else if (newRleString.charAt(i) == '1'){
                rleData[i] = 1;
            } else if (newRleString.charAt(i) == '2'){
                rleData[i] = 2;
            } else if (newRleString.charAt(i) == '3'){
                rleData[i] = 3;
            } else if (newRleString.charAt(i) == '4'){
                rleData[i] = 4;
            } else if (newRleString.charAt(i) == '5'){
                rleData[i] = 5;
            } else if (newRleString.charAt(i) == '6'){
                rleData[i] = 6;
            } else if (newRleString.charAt(i) == '7'){
                rleData[i] = 7;
            } else if (newRleString.charAt(i) == '8'){
                rleData[i] = 8;
            } else if (newRleString.charAt(i) == '9'){
                rleData[i] = 9;
            } else if (newRleString.charAt(i) == 'a'){
                rleData[i] = 10;
            } else if (newRleString.charAt(i) == 'b'){
                rleData[i] = 11;
            } else if (newRleString.charAt(i) == 'c'){
                rleData[i] = 12;
            } else if (newRleString.charAt(i) == 'd'){
                rleData[i] = 13;
            } else if (newRleString.charAt(i) == 'e'){
                rleData[i] = 14;
            } else if (newRleString.charAt(i) == 'f'){
                rleData[i] = 15;
            }
        }
        return rleData;
    }
    public static void printMenu(){
        //just a simple method to avoid repeating the same println(s) again and again
        System.out.println("RLE Menu");
        System.out.println("--------");
        System.out.println("0. Exit");
        System.out.println("1. Load File");
        System.out.println("2. Load Test Image");
        System.out.println("3. Read RLE String");
        System.out.println("4. Read RLE Hex String");
        System.out.println("5. Read Data Hex String");
        System.out.println("6. Display Image");
        System.out.println("7. Display RLE String");
        System.out.println("8. Display Hex RLE Data");
        System.out.println("9. Display Hex Flat Data\n");
    }
    public static void main(String[] args) {
        Scanner scnr = new Scanner(System.in);
        System.out.println("Welcome to the RLE image encoder!\n");
        System.out.println("Displaying Spectrum Image:");
        //displays the test rainbow on program startup
        ConsoleGfx.displayImage(ConsoleGfx.testRainbow);
        System.out.println("");
        //boolean to run the while loop
        boolean isLooping = true;
        //byte array of the current image (to be displayed in console)
        byte[] currentImage = null;
        String stringHolder = "";
        do {
            //always prints out menu every loop
            printMenu();
            System.out.print("Select a Menu Option: ");
            int selectedOption = scnr.nextInt();
            if (selectedOption == 0) {
                //ends program
                isLooping = false;
            } else if (selectedOption == 1) {
                System.out.print("Enter name of file to load: ");
                String imageFileName = scnr.next();
                //accepts file name and loads corresponding byte array to currentImage
                currentImage = ConsoleGfx.loadFile(imageFileName);
            } else if (selectedOption == 2) {
                System.out.println("Test image data loaded.");
                //loads the test image data into currentImage
                currentImage = ConsoleGfx.testImage;
            } else if (selectedOption == 3) {
                System.out.print("Enter an RLE string to be decoded: ");
                stringHolder = scnr.next();
                //decodes the user inputted RLE string and sets currentImage equal to it
                currentImage = decodeRle(stringToRle(stringHolder));
            } else if (selectedOption == 4) {
                System.out.print("Enter the hex string holding RLE data: ");
                stringHolder = scnr.next();
                //decodes the user inputted RLE string and sets currentImage equal to it
                currentImage = decodeRle(stringToRle(stringHolder));
            } else if (selectedOption == 5) {
                System.out.print("Enter the hex string holding flat data: ");
                stringHolder = scnr.next();
                //decodes the user inputted hex string and sets currentImage equal to it
                currentImage = decodeRle(stringToData(stringHolder));
            } else if (selectedOption == 6) {
                System.out.println("Displaying image...");
                //displays currentImage
                ConsoleGfx.displayImage(currentImage);
            } else if (selectedOption == 7) {
                //essentially "reverses" currentImage into an equivalent RLE string
                System.out.println("RLE representation: " + toRleString(encodeRle(currentImage)));
            } else if (selectedOption == 8) {
                //essentially "reverses" currentImage into an equivalent RLE hex string and removes colons
                String placeholder = toRleString(encodeRle(currentImage));
                placeholder = placeholder.replaceAll(":","");
                System.out.println("RLE hex values: " + placeholder);
            } else if (selectedOption == 9) {
                //"turns" byte array of currentImage into a corresponding hex string
                System.out.println("Flat hex values: " + toHexString(currentImage));
            } else {
                //"returns" error and runs loop again
                System.out.println("Error! Invalid input.\n");
            }
        } while (isLooping);
    }
}
