use serde::de::DeserializeOwned;
use tauri::{plugin::PluginApi, AppHandle, Runtime};

use crate::models::*;

pub fn init<R: Runtime, C: DeserializeOwned>(
    app: &AppHandle<R>,
    _api: PluginApi<R, C>,
) -> crate::Result<SafeAreaInsetsCss<R>> {
    Ok(SafeAreaInsetsCss(app.clone()))
}

/// Access to the safe-area-insets-css APIs.
pub struct SafeAreaInsetsCss<R: Runtime>(AppHandle<R>);

impl<R: Runtime> SafeAreaInsetsCss<R> {
    pub fn get_top_inset(&self) -> crate::Result<GetInsetResponse> {
        Ok(GetInsetResponse { inset: 0.0 })
    }

    pub fn get_bottom_inset(&self) -> crate::Result<GetInsetResponse> {
        Ok(GetInsetResponse { inset: 0.0 })
    }

    pub fn get_edge_insets(&self) -> crate::Result<GetEdgeInsetsResponse> {
        Ok(GetEdgeInsetsResponse {
            top: 0.0,
            right: 0.0,
            bottom: 0.0,
            left: 0.0,
        })
    }
}
