[![Release](https://jitpack.io/v/PushDevonics/push-devonics-unity.svg)](https://jitpack.io/#PushDevonics/push-devonics-unity)

# push-devonics-unity
Push Devonics Unity Plugin

Attention to use this library, you must update the Target Android SDK to version 33

Download and Import push-devonyx.unitypackage into you Unity Project.

Initialization:

using DevonicsPush;

```csharp
public class YouClass : MonoBehaviour
{
    void Awake()
    {
        PushDevonics.InitPush("YOU-APP-PUSH-KEY");
    }
}
```

If you need Open Url From Push:

```csharp
PushDevonics.OpenPushUrl();
```

If you need Internal Push User ID:

```csharp
string id = PushDevonics.GetInternalID();
```
