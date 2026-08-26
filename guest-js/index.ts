import { invoke,addPluginListener, PluginListener } from '@tauri-apps/api/core'
interface GetInsetResponse {
  inset: number
}

interface GetEdgeInsetsResponse {
  top: number;
  right: number;
  bottom: number;
  left: number;
}

export async function getTopInset(): Promise<GetInsetResponse | null> {
  return await invoke<GetInsetResponse>('plugin:safe-area-insets-css-edge|get_top_inset', {
    payload: {},
  });
}

export async function getBottomInset(): Promise<GetInsetResponse | null> {
  return await invoke<GetInsetResponse>('plugin:safe-area-insets-css-edge|get_bottom_inset', {
    payload: {},
  });
}

export async function getEdgeInsets(): Promise<GetEdgeInsetsResponse | null> {
  return await invoke<GetEdgeInsetsResponse>("plugin:safe-area-insets-css-edge|get_edge_insets", {
      payload: {},
  });
}

export async function onKeyboardShown(
  handler: () => void
): Promise<PluginListener> {
  return await addPluginListener(
    'safe-area-insets-css-edge',
    'keyboard_shown',
    handler
  );
}

export async function onKeyboardHidden(
  handler: () => void
): Promise<PluginListener> {
  return await addPluginListener(
    'safe-area-insets-css-edge',
    'keyboard_hidden',
    handler
  );
}

export async function onEdgeInsetsChanged(
  handler: (insets: GetEdgeInsetsResponse) => void
): Promise<PluginListener> {
  return await addPluginListener(
    "safe-area-insets-css-edge",
    "edge-insets-changed",
    handler
  );
}

async function init() {
  const topInset = await getTopInset();
  const bottomInset = await getBottomInset();
  const edgeInsets = await getEdgeInsets();
  if (topInset) {
    document.documentElement.style.setProperty('--safe-area-inset-top', `${topInset?.inset}px`);
  }
  if (bottomInset) {
    document.documentElement.style.setProperty('--safe-area-inset-bottom', `${bottomInset?.inset}px`);
  }
  if (edgeInsets) {
    document.documentElement.style.setProperty("--edge-top", `${edgeInsets?.top}px`);
    document.documentElement.style.setProperty("--edge-right", `${edgeInsets?.right}px`);
    document.documentElement.style.setProperty("--edge-bottom", `${edgeInsets?.bottom}px`);
    document.documentElement.style.setProperty("--edge-left", `${edgeInsets?.left}px`);
  }

  await onKeyboardShown(() => {
    document.documentElement.style.setProperty('--safe-area-inset-bottom', `0px`);
    document.documentElement.style.setProperty('--edge-bottom', `0px`);
  });

  await onKeyboardHidden(() => {
    document.documentElement.style.setProperty('--safe-area-inset-bottom', `${bottomInset?.inset}px`);
    document.documentElement.style.setProperty('--edge-bottom', `${edgeInsets?.bottom}px`);
  });

  await onEdgeInsetsChanged((edgeInsets) => {
    document.documentElement.style.setProperty("--edge-top", `${edgeInsets?.top}px`);
    document.documentElement.style.setProperty("--edge-right", `${edgeInsets?.right}px`);
    document.documentElement.style.setProperty("--edge-bottom", `${edgeInsets?.bottom}px`);
    document.documentElement.style.setProperty("--edge-left", `${edgeInsets?.left}px`);
  });
}

async function waitForTauritoLoad() {
  while (typeof (window as any).__TAURI_INTERNALS__ === "undefined") {
    await new Promise((resolve) => setTimeout(resolve, 50)); // check toutes les 50ms
  }
  init();
}
waitForTauritoLoad()