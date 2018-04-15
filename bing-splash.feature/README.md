`osgi > ss p2.console`

get the installed bundle number, say 135

`osgi > start 135`

```bash
provlpquery this "select(feature | feature.providedCapabilities.exists(p | p.namespace == 'org.eclipse.update.feature') == true )" true
```