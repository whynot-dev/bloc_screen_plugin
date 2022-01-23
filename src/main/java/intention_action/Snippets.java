package intention_action;

public class Snippets {
    public static final String PREFIX_SELECTION = "Subject";

    public static final String SUFFIX1 = "Bloc";
    public static final String SUFFIX2 = "State";

    public static final String BLOC_SNIPPET_KEY = PREFIX_SELECTION + SUFFIX1;
    public static final String STATE_SNIPPET_KEY = PREFIX_SELECTION + SUFFIX2;

    static String getSnippet(SnippetType snippetType, String widget) {
        if (snippetType == SnippetType.BlocListener) {
            return blocListenerSnippet(widget);
        }
        return blocBuilderSnippet(widget);
    }

    private static String blocBuilderSnippet(String widget) {
        return String.format("BlocBuilder<%1$s, %2$s>(\n" +
                "  buildWhen: (previous, current) => previous.subject != current.subject,\n" +
                "  builder: (context, state) => %3$s,\n" +
                ")", BLOC_SNIPPET_KEY, STATE_SNIPPET_KEY, widget);
    }

    private static String blocListenerSnippet(String widget) {
        return String.format("BlocListener<%1$s, %2$s>(\n" +
                "  listener: (context, state) {\n" +
                "    // TODO: implement listener}\n" +
                "  },\n" +
                "  child: %3$s,\n" +
                ")", BLOC_SNIPPET_KEY, STATE_SNIPPET_KEY, widget);
    }
}
