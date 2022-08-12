package ua.ideabank.obank.data.repository.mapper.base

abstract class BaseMapper<RepoModel : Any, DbModel : Any, NetModel : Any> {
	
	open fun mapDbToModel(dbModel: DbModel): RepoModel = error("Unsupported operation!")
	
	open fun mapModelToDb(repoModel: RepoModel): DbModel = error("Unsupported operation!")
	
	open fun mapNetToModel(netModel: NetModel): RepoModel = error("Unsupported operation!")
	
	open fun mapNetToDb(netModel: NetModel): DbModel = error("Unsupported operation!")
	
	open fun mapDbToModel(dbModel: List<DbModel>): List<RepoModel> = dbModel.map(::mapDbToModel)
	
	open fun mapModelToDb(repoModel: List<RepoModel>): List<DbModel> = repoModel.map(::mapModelToDb)
	
	open fun mapNetToModel(netModel: List<NetModel>): List<RepoModel> = netModel.map(::mapNetToModel)
	
	open fun mapNetToDb(netModel: List<NetModel>): List<DbModel> = netModel.map(::mapNetToDb)
}