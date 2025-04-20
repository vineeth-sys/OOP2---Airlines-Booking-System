package airline;

import java.util.Map;

public class FareCalculator {
    private static final Map<CabinType, Integer> fareMap = Map.of(
        CabinType.ECONOMY, 100,
        CabinType.BUSINESS, 200,
        CabinType.FIRST_CLASS, 300
    );

    public static int calculateFare(CabinType cabinType) {
        return fareMap.getOrDefault(cabinType, 100);
    }
}
