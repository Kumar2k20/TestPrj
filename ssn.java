mport java.util.Map;
import java.util.HashMap;

public class SSNValidator {

    // Static map for area number to state mapping (pre-2011)
    private static final Map<Integer, String> pre2011AreaToStateMap = new HashMap<>();

    static {
        // Example entries for demonstration
        pre2011AreaToStateMap.put(001, "New Hampshire");
        pre2011AreaToStateMap.put(002, "Maine");
        pre2011AreaToStateMap.put(003, "Massachusetts");
        // Add other mappings as needed
        // Note: In a real implementation, this map should be fully populated with all pre-2011 area numbers and states
    }

    // Function to find a valid SSN in a given sentence
    public static String findValidSSN(String sentence) {
        // Define the SSN pattern
        String ssnPattern = "\\b(?!666|000|9\\d\\d)(\\d{3})-(?!00)(\\d{2})-(?!0000)(\\d{4})\\b";
       
        // Compile the pattern
        Pattern pattern = Pattern.compile(ssnPattern);
        Matcher matcher = pattern.matcher(sentence);

        // Search for SSN in the sentence
        while (matcher.find()) {
            String ssn = matcher.group();
            if (isValidSSN(ssn)) {
                return ssn; // Return the first valid SSN found
            }
        }
        return "No valid SSN found.";
    }

    // Function to validate the SSN according to specific rules
    private static boolean isValidSSN(String ssn) {
        // Split the SSN into its components
        String[] parts = ssn.split("-");
        int area = Integer.parseInt(parts[0]);
        int group = Integer.parseInt(parts[1]);
        int serial = Integer.parseInt(parts[2]);

        // Check the area number rules
        if (area == 0 || area == 666 || area >= 900) {
            return false;
        }

        // Check the group number rules
        if (group == 0) {
            return false;
        }

        // Check the serial number rules
        if (serial == 0) {
            return false;
        }

        // Check if the SSN is sequential
        if (isSequential(ssn)) {
            return false;
        }

        // Check if the SSN is all the same digit
        if (isAllSameDigit(ssn)) {
            return false;
        }

        // Check if the SSN contains only zeros in any part
        if (containsOnlyZeros(ssn)) {
            return false;
        }

        // Check the total number of digits
        if (!hasValidDigitCount(ssn)) {
            return false;
        }

        // Pre-2011 Rules: Check if the area number is valid geographically (only applies to SSNs issued before 2011)
        if (isPre2011SSN(ssn)) {
            // Additional checks can be added here if needed
        }

        // Check against Death Master File (DMF)
        if (isInDeathMasterFile(ssn)) {
            return false;
        }

        // All checks passed
        return true;
    }

    // Helper function to check if SSN is sequential
    private static boolean isSequential(String ssn) {
        // Remove dashes for simplicity
        ssn = ssn.replace("-", "");
        // Check if the digits are sequential
        for (int i = 0; i < ssn.length() - 1; i++) {
            if (ssn.charAt(i + 1) != ssn.charAt(i) + 1) {
                return false;
            }
        }
        return true;
    }

    // Helper function to check if SSN is all the same digit
    private static boolean isAllSameDigit(String ssn) {
        // Remove dashes for simplicity
        ssn = ssn.replace("-", "");
        char firstChar = ssn.charAt(0);
        // Check if all characters are the same
        for (int i = 1; i < ssn.length(); i++) {
            if (ssn.charAt(i) != firstChar) {
                return false;
            }
        }
        return true;
    }

    // Helper function to check if any part of the SSN contains only zeros
    private static boolean containsOnlyZeros(String ssn) {
        String[] parts = ssn.split("-");
        for (String part : parts) {
            if (part.matches("0+")) {
                return true;
            }
        }
        return false;
    }

    // Helper function to check if the SSN has exactly 9 digits
    private static boolean hasValidDigitCount(String ssn) {
        String digitsOnly = ssn.replace("-", "");
        return digitsOnly.length() == 9;
    }

    // Helper function to check if SSN is from pre-2011 (geographic check)
    private static boolean isPre2011SSN(String ssn) {
        int area = Integer.parseInt(ssn.split("-")[0]);
        return pre2011AreaToStateMap.containsKey(area);
    }

    // Helper function to check if SSN is in the Death Master File (DMF)
    private static boolean isInDeathMasterFile(String ssn) {
        // This function should check the SSN against the DMF
        // For this example, we return false to indicate the SSN is not in the DMF
        // In a real implementation, this would involve checking a database or file
        return false;
    }

    // Main function to test the SSN validator
    public static void main(String[] args) {
        String sentence = "Here is a sentence with a SSN 123-45-6789 and another one 000-12-3456.";
        String validSSN = findValidSSN(sentence);
        System.out.println("Valid SSN found: " + validSSN);
    }
}