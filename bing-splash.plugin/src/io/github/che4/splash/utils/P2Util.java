package io.github.che4.splash.utils;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.engine.IProfile;
import org.eclipse.equinox.p2.engine.IProfileRegistry;
import org.eclipse.equinox.p2.metadata.IArtifactKey;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.IProvidedCapability;
import org.eclipse.equinox.p2.query.IQuery;
import org.eclipse.equinox.p2.query.IQueryResult;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.osgi.framework.Bundle;

import io.github.che4.splash.Activator;

public class P2Util {
	
	protected static final String NAMESPACE_ECLIPSE_TYPE = "org.eclipse.equinox.p2.eclipse.type";
	protected static final String TYPE_ECLIPSE_FEATURE = "feature";
	protected static final String SPLASH_PROPERTY_NAME = "osgi.splashPath"; //$NON-NLS-1$
	protected static final String SPLASH_PREFIX = "platform:/base/features/"; //$NON-NLS-1$
	protected static final String CAPABILITY_NS_UPDATE_FEATURE = "org.eclipse.update.feature";

	//FIXME remove late!!!!!!!!!!!!
	//private static final TxtFile log = new TxtFile();
	/**
	 * Gets a set of latest features, containing this bundle &mdash; specified by <code>bundleId</code> and <code>version</code>. 
	 * @param bundleId the ID of a bundle we want to find features containing it.
	 * @param version considered if <strong>NOT</strong> <code>{@linkplain org.osgi.framework.Version#emptyVersion empty}</code> or <code>null</code>,
	 * otherwise will be dropped.
	 * @return a set (may be empty) of features as IInstallableUnit's that embrace <code>the bundle</code> defined by <code>bundleId</code>
	 * @throws IllegalArgumentException if <code>bundleId</code> is <code>null</code>
	 */
	public static Set<IInstallableUnit> findFeaturesByBundle(String bundleId, String version){
		Objects.requireNonNull( bundleId, "bundleId is null" );
		Optional<IProvisioningAgent> optProvAgent = Activator.getProvisioningAgent();
		
		if(optProvAgent.isPresent()) {
			IProvisioningAgent provisioningAgent = optProvAgent.get();
			IProfileRegistry profileRegistry = (IProfileRegistry) provisioningAgent.getService(IProfileRegistry.SERVICE_NAME);
			IProfile p2Profile = profileRegistry.getProfile(IProfileRegistry.SELF);
			if(p2Profile!=null){
				//'org.eclipse.equinox.p2.type.group' == QueryUtil.PROP_TYPE_GROUP
				IQuery<IInstallableUnit> query = null;
				try{
					org.osgi.framework.Version osgiVersion = org.osgi.framework.Version.parseVersion(version);
					if(osgiVersion!=null && !osgiVersion.equals(org.osgi.framework.Version.emptyVersion)){
						query = QueryUtil.createQuery("latest(parent | parent.properties[$0] == true && parent.requirements.exists(rc | everything.exists(iu | iu.id == $1 && iu.version == $2 && iu ~= rc)))", QueryUtil.PROP_TYPE_GROUP, bundleId, version);
					}
				}catch(IllegalArgumentException e){}
				//version is empty, e.g 0.0.0
				if(query == null){
					query = QueryUtil.createQuery("latest(parent | parent.properties[$0] == true && parent.requirements.exists(rc | everything.exists(iu | iu.id == $1 && iu ~= rc)))", QueryUtil.PROP_TYPE_GROUP, bundleId);
				}
				IQueryResult<IInstallableUnit> result = p2Profile.query(query, null);
				return result.toSet();
			}else{
				return Collections.emptySet();
			}
		}
		return Collections.emptySet();
	}
	
	public static Set<IInstallableUnit> findFeature(Bundle bundle){
		Objects.requireNonNull( bundle, "bundle is null" );
		Optional<IProvisioningAgent> optProvAgent = Activator.getProvisioningAgent();
		
		if(optProvAgent.isPresent()) {
			IProvisioningAgent provisioningAgent = optProvAgent.get();
			IProfileRegistry profileRegistry = (IProfileRegistry) provisioningAgent.getService(IProfileRegistry.SERVICE_NAME);
			IProfile p2Profile = profileRegistry.getProfile(IProfileRegistry.SELF);
			if(p2Profile!=null){
				//'org.eclipse.equinox.p2.type.group' == QueryUtil.PROP_TYPE_GROUP
				IQuery<IInstallableUnit> query = null;
				try{
					if(!bundle.getVersion().equals(org.osgi.framework.Version.emptyVersion)){
						query = QueryUtil.createQuery("latest(parent | parent.properties[$0] == true && parent.requirements.exists(rc | everything.exists(iu | iu.id == $1 && iu.version == $2 && iu ~= rc)))", 
								QueryUtil.PROP_TYPE_GROUP, bundle.getSymbolicName(), bundle.getVersion().toString());
					}
				}catch(IllegalArgumentException e){}
				//version is empty, e.g 0.0.0
				if(query == null){
					query = QueryUtil.createQuery("latest(parent | parent.properties[$0] == true && parent.requirements.exists(rc | everything.exists(iu | iu.id == $1 && iu ~= rc)))", QueryUtil.PROP_TYPE_GROUP, bundle.getSymbolicName());
				}
				IQueryResult<IInstallableUnit> result = p2Profile.query(query, null);
				return result.toSet();
			}else{
				return Collections.emptySet();
			}
		}
		return Collections.emptySet();
	}
	
	public static Optional<File> findFeatureDir(Bundle bundle) throws IOException{
		Objects.requireNonNull( bundle, "bundle is null" );
		Optional<IProvisioningAgent> optProvAgent = Activator.getProvisioningAgent();
		
		if(optProvAgent.isPresent()) {
			IProvisioningAgent provisioningAgent = optProvAgent.get();
			IProfileRegistry profileRegistry = (IProfileRegistry) provisioningAgent.getService(IProfileRegistry.SERVICE_NAME);
			IProfile[] profiles = profileRegistry.getProfiles();
			
			//log.appendLine("Profiles: " + profiles.length);
			IProfile p2Profile = profileRegistry.getProfile(IProfileRegistry.SELF);
			//log.appendLine("Self profile: " + p2Profile.getProfileId());
			if(p2Profile!=null){
				//'org.eclipse.equinox.p2.type.group' == QueryUtil.PROP_TYPE_GROUP
				IQuery<IInstallableUnit> query = null;
				try{
					if(!bundle.getVersion().equals(org.osgi.framework.Version.emptyVersion)){
						query = QueryUtil.createQuery("latest(parent | parent.properties[$0] == true && parent.requirements.exists(rc | everything.exists(iu | iu.id == $1 && iu.version == $2 && iu ~= rc)))", 
								QueryUtil.PROP_TYPE_GROUP, bundle.getSymbolicName(), bundle.getVersion().toString());
					}
				}catch(IllegalArgumentException e){}
				//version is empty, e.g 0.0.0
				if(query == null){
					query = QueryUtil.createQuery("latest(parent | parent.properties[$0] == true && parent.requirements.exists(rc | everything.exists(iu | iu.id == $1 && iu ~= rc)))", QueryUtil.PROP_TYPE_GROUP, bundle.getSymbolicName());
				}
				IQueryResult<IInstallableUnit> result = p2Profile.query(query, null);
				Set<IInstallableUnit> resultSet = result.toSet();
				//log.appendLine("Result: " + resultSet.size());
				Optional<IInstallableUnit> oIiu = result.toSet().stream().findFirst();
				if(oIiu.isPresent()) {
					IInstallableUnit iiu = oIiu.get();
					//log.appendLine("InstallableUnits found: " + iiu.getId());
					Optional<IInstallableUnit> oFeatureJar = findFeatureJar(iiu, p2Profile);
					if(oFeatureJar.isPresent()) {
						IInstallableUnit featureJar = oFeatureJar.get();
						Collection<IArtifactKey> artifacts = featureJar.getArtifacts();
						//log.appendLine("Feature.jar has artifacts: " + !artifacts.isEmpty());
						if(!artifacts.isEmpty()) {
							IArtifactKey artifactKey = artifacts.iterator().next();
							//log.appendLine("ArtifactKey: " + artifactKey.getId());
							@SuppressWarnings("restriction")
							File file = org.eclipse.equinox.internal.p2.touchpoint.eclipse.Util.getArtifactFile(provisioningAgent, artifactKey, p2Profile);
							//log.appendLine("ArtifactFile: " + file.getAbsolutePath());
							return Optional.ofNullable(file);
						}
					}
				}
			}
		}
		return Optional.empty();
	}
	
	private static Optional<IInstallableUnit> findFeatureJar(IInstallableUnit featureGroup, IProfile profile) throws IOException {
		//log.appendLine("Search feature jar for " + featureGroup.getId());
		IQuery<IInstallableUnit> query = QueryUtil.createQuery("latest(f|f.providedCapabilities.exists(cap|cap.namespace==$0 && cap.name==$1)"+
				" && everything.exists(iu|iu.id==$2 && iu.requirements.exists(rc| f~=rc)))",
					NAMESPACE_ECLIPSE_TYPE, TYPE_ECLIPSE_FEATURE, featureGroup.getId());
			
		
			IQueryResult<IInstallableUnit> result = profile.query(query, null);
			//log.appendLine("Feature jar found: " + !result.isEmpty());
			if(!result.isEmpty()) {
				IInstallableUnit featureJar = result.iterator().next();
				Collection<IProvidedCapability> caps = featureJar.getProvidedCapabilities();
				Iterator<IProvidedCapability> iter = caps.iterator();
				//log.appendLine("Confirming that it is feature.jar...");
				while(iter.hasNext()) {		
					IProvidedCapability cap = iter.next();
					String namespace = cap.getNamespace();
					if(namespace != null && namespace.equals(CAPABILITY_NS_UPDATE_FEATURE)) {
						//log.appendLine("YEP! " + featureJar.getId() + " is feature.jar");
						return Optional.of(featureJar);
					}
				}
			}
			return Optional.empty();
	}

}
