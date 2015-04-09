
# NICHE: Name Inconsistency CHEcker

40% to 60% of software maintenance effort is devoted to understanding programs. When software documentation becomes out of date or does not exist at all, developers need code to be self-explanatory. Developers are therefore encouraged to assign meaningful names to identifiers.  

Instead of using hard-coded rules, Niche mines naming rules based on two insights: (1) Pure and impure method names should reflect their purity property. (2) Objects with similar functionality should have similar names. First, Niche uses purity analysis to classify terms in method names as either pure terms or impure terms. Second, Niche implements type-use pattern analysis. For each object within each method, Niche models a sequence of statements that read from or write to the object or invoke methods on it using a def-use analysis. It then clusters similar type-use patterns and identifies object names that deviate from commonly used object names. 
