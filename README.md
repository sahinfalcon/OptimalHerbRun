# OptimalHerbRun

![License](https://img.shields.io/github/license/sahinfalcon/OptimalHerbRun)

A RuneLite plugin in development that aims to optimize herb farming runs through patch tracking, route optimization, and profit calculations.

## Planned Features

### Patch Tracking
- Growth stage monitoring
- Disease/protection status
- Visual map indicators
- Time-until-harvest tracking

### Route Optimization
- Efficient path suggestions based on available teleports
- Time estimates for run completion
- Inventory-aware routing

### Profit Analysis
- GE price integration
- Run profitability calculations
- Protection cost vs. profit analysis

### Notifications
- Patch readiness alerts
- Disease warnings
- Profit threshold notifications

## Development Status

This plugin is currently under initial development. Features are being implemented and tested. Not yet available in the Plugin Hub.

## Planned FAQ

**Q: How will the patch tracking work?**  
A: When implemented, the plugin will start tracking a herb patch once you first interact with it. This allows the plugin to:
- Confirm your access to the patch
- Record its growth cycle
- Monitor its status

**Q: Will the route system work with my available teleports?**  
A: The plugin will scan your inventory, equipment, and completed achievement diaries to suggest the best possible routes using your available transportation methods.

**Q: How will profit calculations handle market changes?**  
A: The plugin will integrate with RuneLite's GE pricing data, updating regularly to provide current profit estimates based on:
- Seed costs
- Herb prices
- Protection costs
- Teleport expenses

**Q: What about disease-free patches?**  
A: The plugin will recognize special patches (like the Farming Guild's) and adjust its protection recommendations accordingly.

## Development

This is an open-source project. Once the initial implementation is complete, contribution guidelines will be provided here.

### Current Setup
```bash
git clone https://github.com/sahinfalcon/OptimalHerbRun.git
cd OptimalHerbRun
```

## License

BSD 2-Clause License - see [LICENSE](LICENSE)

---

By Taylan Sahin