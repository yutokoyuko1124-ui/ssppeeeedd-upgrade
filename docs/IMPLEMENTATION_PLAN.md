# Implementation Plan (v1)

## 1. Core Mechanics

### Upgrade effect model

- Base speed multiplier: `1.0`
- Per card speed bonus: `+0.10`
- Per card energy bonus: `+0.02`
- Card count cap: `64`

Formulas:

- `speedMultiplier = 1.0 + (cardCount * 0.10)`
- `energyMultiplier = 1.0 + (cardCount * 0.02)`

At 64 cards:

- `speedMultiplier = 7.4x`
- `energyMultiplier = 2.28x`

## 2. Compatibility Strategy

Because introducing a brand-new Mekanism upgrade type is intrusive, v1 should:

1. Register a custom card item for player-facing UX.
2. Resolve that card to Mekanism Speed-upgrade semantics internally.
3. Intercept/extend upgrade count logic to allow up to 64 effective speed cards.

## 3. Data & Content

- `en_us.json` language entries
- Crafting recipe JSON for the card
- `mods.toml` metadata with Mekanism dependency constraint

## 4. Technical Notes

- Primary target: machines with upgrade slots.
- Ensure Energy Upgrade co-existence (no replacement behavior).
- Keep behavior deterministic in singleplayer and dedicated server.

## 5. Validation Checklist

- Card can be crafted and inserted.
- Machine processing speed increases per card as expected.
- Energy usage scales per card as expected.
- Machines reject insertion above cap (64).
- Existing Mekanism upgrades still function normally.

## 6. v2 Expansion Targets

- Additional high-tier card line.
- Configurable multipliers/caps.
- Per-machine whitelist/blacklist or scaling profile.
- GUI display of effective speed/energy multipliers.
