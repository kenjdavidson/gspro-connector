package kjd.gspro.app.bridge;

import org.springframework.stereotype.Component;

import kjd.gspro.client.Client;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class BridgeService {
    private Client gsproClient;
    
    public BridgeService() {
        
    }
}
