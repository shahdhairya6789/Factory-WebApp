package com.example.demo.service.serviceImpl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.demo.constants.ApplicationConstants;

@Service
public class SendSMS {
    private Logger logger = LoggerFactory.getLogger(SendSMS.class);

    public void sendSms(String phoneNumber, int otp) {
        try {
            // Construct data
            String apiKey = "apikey=" + "NzA2NTY5NmI1NjM4NGY3NTQxMzk1MTU0NDY0NDQ4NGQ=";
            String message = "&message=" + String.format(ApplicationConstants.OTP_MESSAGE , otp);
            String sender = "&sender=" + ApplicationConstants.OTP_SENDER;
            String numbers = "&numbers=" + phoneNumber;

            // Send data
            HttpURLConnection conn = (HttpURLConnection) new URL("https://api.txtlocal.com/send/?").openConnection();
            String data = apiKey + numbers + message + sender;
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
            conn.getOutputStream().write(data.getBytes(StandardCharsets.UTF_8));
            final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            final StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = rd.readLine()) != null) {
                stringBuffer.append(line);
            }
            logger.info(stringBuffer.toString());
            rd.close();

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Error SMS " + e);
        }
    }
}
