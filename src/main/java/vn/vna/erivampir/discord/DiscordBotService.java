package vn.vna.erivampir.discord;

import org.springframework.stereotype.Service;

@Service
public class DiscordBotService {
    public void awake(String[] args) {
        System.out.println("Discord Bot Service has started");
    }
}
