class Solution {
    public int numUniqueEmails(String[] emails) {
        // Store normalized email addresses to avoid duplicates.
        Set<String> uniqueEmails = new HashSet<>();

        for (String email : emails) {
            // Split the email into local name and domain.
            String[] emailParts = email.split("@");

            String localName = emailParts[0];
            String domain = emailParts[1];

            // Ignore everything after '+' in the local name.
            localName = localName.split("\\+")[0];

            // Remove all '.' characters from the local name.
            localName = localName.replace(".", "");

            // Reconstruct the normalized email address.
            String normalizedEmail = localName + "@" + domain;

            uniqueEmails.add(normalizedEmail);
        }

        return uniqueEmails.size();
    }
}
