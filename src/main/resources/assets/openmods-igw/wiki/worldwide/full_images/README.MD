# Full Images Directory Existence Rationale

Storing full images in a JAR file is very expensive in terms of size and thus space needed.
If we can cut down on the usage, then that can only be good.

By separating the full-sized images from the scaled-down versions we can always decide to not ship this directory in the final production JAR, in order to save size.
At the moment this is not the case, but this can always happen, so better start now than having to manually move thousands and thousands of items later on.
