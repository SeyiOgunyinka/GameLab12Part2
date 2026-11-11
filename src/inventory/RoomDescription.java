package inventory;

import java.io.File;
import java.util.HashMap;
import java.util.Scanner;

public class RoomDescription {

    public static HashMap<String, String> loadDescriptions(
            String filename) {

        HashMap<String, String> descriptions = new HashMap<>();

        try {
            Scanner fileScan = new Scanner(new File(filename));

            String roomName = "";
            String description = "";

            while (fileScan.hasNextLine()) {
                String line = fileScan.nextLine();

                if (line.trim().equals("#")) {
                    if (!roomName.isEmpty()) {
                        descriptions.put(
                                roomName,
                                description.trim());
                    }

                    roomName = "";
                    description = "";

                } else if (roomName.isEmpty()) {
                    if (!line.trim().isEmpty()) {
                        roomName = line.trim();
                    }

                } else {
                    description += line + "\n";
                }
            }

            if (!roomName.isEmpty()) {
                descriptions.put(roomName, description.trim());
            }

            fileScan.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return descriptions;
    }
}
