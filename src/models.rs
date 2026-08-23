use serde::{Deserialize, Serialize};

#[derive(Debug, Clone, Default, Deserialize, Serialize)]
#[serde(rename_all = "camelCase")]
pub struct GetInsetResponse {
    pub inset: f64,
}

#[derive(Debug, Clone, Default, Deserialize, Serialize)]
#[serde(rename_all = "camelCase")]
pub struct GetEdgeInsetsResponse {
    pub top: f64,
    pub right: f64,
    pub bottom: f64,
    pub left: f64,
}