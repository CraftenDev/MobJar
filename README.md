# MobJar
MobJar is a Bukkit plugin that allows you to catch mobs¹ with jars and restore them.
This makes trading, transport and storage of mobs easy.

We tested this on Bukkit 1.8 and 1.9, but the plugin should work fine on 1.10 and 1.11, as it doesn't use NMS classes.

¹ For now, only horses and wolves are supported. [File an issue][issues] if you want to have jars for more creatures.

## Usage
There is only one command, `/jar`. This command is used to buy a mob jar. Vault is required to withdraw the price. If
you don't have Vault or don't have an economy plugin, jars will be free.

To catch a mob, right-click it with a jar. To restore a mob from a jar, left-click with that jar.
You can also drink from jars of mountable mobs (e.g. horses) to restore the mob and mount it.

## Permissions
| Permission   | Default | Description |
|--------------|---------|-------------|
| `mobjar.jar` | `true`  | Allows buying jars using `/jar`. |
| `mobjar.use` | `true`  | Allows catching mobs with jars.  |
| `mobjar.takemounted` | `op` | Allows putting mobs into jars while another player rides on that mob. |
| `mobjar.steal` | `op` | Allows putting mobs into jars that are tamed by other players. |
| `mobjar.free` | `false` | Players with this permission get jars for free. |
| `mobjar.*` | `false` | Includes all permissions except `mobjar.free`.

## Configuration
| Option | Default | Description |
|--------|---------|-------------|
| `jarprice` | 500 | The price of a mob jar. |

## License
MobJar is licensed under the MIT license, see the [license file][license].

[issues]: https://github.com/CraftenDev/MobJar/issues
[license]: https://github.com/CraftenDev/MobJar/blob/master/LICENSE
