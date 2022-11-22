package ngavax.app;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


class proxyHandler{

    public ArrayList<String> headerArray(String HEADERS, String xReal){
        ArrayList<String> hl = new ArrayList<String>();
        String[] lines = HEADERS.split("\n");
        String xHost = "";
        for (String line : lines) {
            if(!line.contains("GET"))
                if(line.contains("Host: ")){
                    xHost = line.split(": ")[1];
                }else{
                    hl.add(line.split(":")[0]);
                    hl.add(line.split(":")[1]);
                }
        }
        hl.add("X-Forwarded-Proto");
        hl.add("http");
        hl.add("X-Forwarded-Host");
        hl.add(xHost);
        hl.add("X-Real-IP");
        hl.add(xReal);

        return hl;
    }

    public byte[] proxyPass(String address, ArrayList<String> headerArr){
        try {
            URL url = new URL(address);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            for (int i = 0; i < headerArr.size(); i+=2) {
                LOG.info(headerArr.get(i) + ": " + headerArr.get(i+1));
                connection.setRequestProperty(headerArr.get(i), headerArr.get(i+1));
            }
            //connection.setRequestProperty("User-Agent", "Mozilla/5.0");

            int responseCode = connection.getResponseCode();
            if(responseCode == 200){
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                return response.toString().getBytes();
            }else{
                return ("502 - Bad Gateway").getBytes();
            }
        } catch (Exception e) {
            LOG.error(e);
            return ("502 - Bad Gateway").getBytes();
        }
    }
}
