# Multiple permissions checking logic

Multiple permissions for a command can be a good idea to allow a default "group" of
commands. For example the `/lobby` command will have its own `minersonline.commands.lobby`
and allow `minersonline.player`.

This is how the logic works:

1. If both are false then the output is false.
2. If the first one is true and the second one false then the output is false.
3. If the first one is false and the second one is true then the output is true.
4. If both are true then the output is true.

A simple truth table:

| 1 | 2 | Result |
|---|---|--------|
| F | F | F      | 
| F | T | T      |
| T | F | F      |
| T | T | T      |

## ChatGPT attempts

### attempt 1

```java
class LobbyCommand {
	private static boolean calculateOutput(boolean boolean1, boolean boolean2) {
// If both are false, the output is false
		if (!boolean1 && !boolean2) {
			return false;
		}
// If the first one is true and the second one is false, the output is false
		else if (boolean1 && !boolean2) {
			return false;
		}
// If the first one is false and the second one is true, the output is true
		else if (!boolean1 && boolean2) {
			return true;
		}
// If both are true, the output is true
		else {
			return true;
		}		
	}
}
```

This is correct, but long and complicated and could be simplified.

### attempt 2

```
boolean1 = false
boolean2 = true

`(!boolean1 && !boolean2) || (boolean1 && boolean2)`
    true        false           false       true
          false                      false
                        false

result = false
```

Result (`false`) is incorrect, it should be `true`.

### attempt 3

#### test 1

```
boolean1 = false
boolean2 = true

`!(boolean1 && !boolean2)`
    false         true
          false
result = true
```

Result (`true`) is correct.

#### test 2

```
boolean1 = true
boolean2 = false

`!(boolean1 && !boolean2)`
    true          false
          false

result = true
```

Result (`true`) is incorrect, it should be `false`.

## My (@ajh13) attempts

### attempt 1
```
boolean1 = false
boolean2 = true

`(!boolean1 && boolean2) || (boolean1 && boolean2)`
   false          true        false        true
           false                    false
                        flase    
= flase                     
```

Result (`false`) is incorrect.

### attempt 2

```
boolean1 = true
boolean2 = false

`(!boolean1 && boolean2) || (boolean1 && boolean2)`
   true          false        true        false
           false                    false        
                        false   
= false 
```

Result (`false`) is correct.

### attempt 3

```
boolean1 = false
boolean2 = true

`(boolean2 && boolean1) && (boolean2 != false)`
   true         false         true
          false                     true
                      false

= false     
```

Result (`false`) is incorrect.

### attempt 4 (correct)

### test 1

```
boolean1 = false
boolean2 = true

`(boolean2 && boolean1) || (boolean2 != false)`
   true        false         true
          false                     true
                      true

= true
```

Result (`true`) is correct.

### test 2

```
boolean1 = true
boolean2 = false

`(boolean2 && boolean1) || (boolean2 != false)`
   false        true         false
          false                     false
                      false

= false
```

Result (`false`) is correct.

### test 3

```
boolean1 = true
boolean2 = true

`(boolean2 && boolean1) || (boolean2 != false)`
   true        true         true
          true                     true
                      true

= true
```

Result (`true`) is correct.

### test 4

```
boolean1 = false
boolean2 = false

`(boolean2 && boolean1) || (boolean2 != false)`
   false        false         false
          false                     false
                      false

= false
```

Result (`false`) is correct.