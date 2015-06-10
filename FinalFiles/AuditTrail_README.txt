This audit trail file lists matches between sources.  
The first two columns are the source ID's that matched, and the third column
is the rule ID.

To see which rule fired, please look up the rule ID in the rules.ini file, linked in the wiki.
This will have a description of the rules, as well as the weights associated with each attribute

The way our matching works is a weighted sum of attribute similarity. For example, if our name matcher
said that two names were 75% similar by edit distance + Jaro-Winkler, then the value for name = .75 * nameWeight.
Two sources match if the sum of these weighted values over all attributes is greater than the threshold, also
specified per rule in the rules.ini file.