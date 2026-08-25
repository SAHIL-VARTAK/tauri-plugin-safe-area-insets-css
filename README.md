# Tauri Plugin safe-area-insets-css

> This is a fork of the original [tauri-plugin-safe-area-insets-css](https://github.com/saurl/tauri-plugin-safe-area-insets-css) plugin with additional support for exposing all four safe-area edge insets.

A Tauri plugin to expose safe-area insets as CSS variables for your frontend. This is useful for mobile applications where you need to account for notches, rounded corners, system bars, and other system UI elements.

## Rust Side

### Add the crate to your `Cargo.toml`

```toml
[dependencies]
tauri-plugin-safe-area-insets-css-edge = "0.2"
```

### Initialize the plugin in your Tauri application

```rust
fn main() {
    tauri::Builder::default()
        .plugin(tauri_plugin_safe_area_insets_css_edge::init())
        .run(tauri::generate_context!())
        .expect("error while running tauri application");
}
```

## JavaScript / TypeScript Side

Install the JavaScript API:

```bash
import '@sahil-vartak/tauri-plugin-safe-area-insets-css-api';
```

Import it in your JavaScript/TypeScript entry file:

```ts
import '@sahil-vartak/tauri-plugin-safe-area-insets-css-api';
```

After initialization, the following CSS variables are automatically available:

* `--edge-top`
* `--edge-right`
* `--edge-bottom`
* `--edge-left`

These values represent the safe-area inset for each edge in pixels.

You can use them directly in your CSS:

```css
body {
  padding-top: var(--edge-top);
  padding-right: var(--edge-right);
  padding-bottom: var(--edge-bottom);
  padding-left: var(--edge-left);
}
```

For example, the plugin exposes the values as:

```ts
document.documentElement.style.setProperty(
  "--edge-top",
  `${edgeInsets?.top}px`
);

document.documentElement.style.setProperty(
  "--edge-right",
  `${edgeInsets?.right}px`
);

document.documentElement.style.setProperty(
  "--edge-bottom",
  `${edgeInsets?.bottom}px`
);

document.documentElement.style.setProperty(
  "--edge-left",
  `${edgeInsets?.left}px`
);
```

## ⚠️ Important Note

Do not run this plugin outside of a Tauri environment. Doing so will create an infinite loop that can significantly slow down your site.

## Notes

The plugin is configured to automatically set `--edge-bottom` to `0` when the keyboard is visible.

The original plugin supports both iOS and Android.

## Fork Updates

This fork adds `getEdgeInsets()` support and exposes safe-area values for all four edges:

* `--edge-top`
* `--edge-right`
* `--edge-bottom`
* `--edge-left`

This allows applications to account for safe-area insets on every side of the screen instead of only the top and bottom.

## ⚠️ Testing

The original plugin supports both iOS and Android. **The modifications in this fork have currently only been tested on Android.**
