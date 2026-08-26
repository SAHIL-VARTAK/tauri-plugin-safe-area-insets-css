const COMMANDS: &[&str] = &[
    "get_top_inset",
    "get_bottom_inset",
    "get_edge_insets",
    "registerListener",
    "unregister_listener",
];

fn main() {
    tauri_plugin::Builder::new(COMMANDS)
        .android_path("android")
        .ios_path("ios")
        .build();
}
